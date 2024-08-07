package com.pzhu.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.auth.config.JwtProperties;
import com.pzhu.auth.util.AuthenticationUtils;
import com.pzhu.core.exception.BusinessException;
import com.pzhu.core.utils.Md5Utils;
import com.pzhu.core.utils.R;
import com.pzhu.redis.utils.RedisCache;
import com.pzhu.user.dto.UserInfoDTO;
import com.pzhu.user.mapper.UserInfoMapper;
import com.pzhu.user.redis.key.UserRedisKeyPrefix;
import com.pzhu.user.service.UserInfoService;
import com.pzhu.user.domain.UserInfo;
import com.pzhu.user.vo.LoginUser;
import com.pzhu.user.vo.RegisterRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    private final RedisCache redisCache;

    public UserServiceImpl(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public UserInfo findByPhone(String phone) {
        // select * from userinfo where phone = #{phone}
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<UserInfo>()
                .eq("phone", phone);
        return getOne(wrapper);
    }

    @Override
    public void register(RegisterRequest req) {
        //基于手机号查询是否已经存在
        UserInfo userInfo = this.findByPhone(req.getPhone());
        if (userInfo != null){
            throw new BusinessException(R.CODE_REGISTER_ERROR,"手机号已经存在，请登录");
        }
        //从redis中取得验证码，与前端传来的验证码进行校验

        UserRedisKeyPrefix keyPrefix = UserRedisKeyPrefix.USER_REGISTER_VERIFY_CODE_STRING;

        String fullKey = redisCache.getCacheObject(keyPrefix.fullKey(req.getPhone()));
        if (!req.getVerifyCode().equalsIgnoreCase(fullKey)){
            throw new BusinessException(R.CODE_REGISTER_ERROR,"验证码错误");
        }
        //将验证码从redis中删除
        redisCache.deleteObject(keyPrefix.fullKey(req.getPhone()));
        //创建用户对象
        userInfo = this.buildUserInfo(req);
        //对密码进行加密
        //加盐+散列（hash）次数  增加密码的安全性
        String encryptPassword = Md5Utils.getMD5(userInfo.getPassword() + userInfo.getPhone());
        userInfo.setPassword(encryptPassword);
        //保存到数据库中   mybatisPlus
        super.save(userInfo);
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        //基于用户名查询用户对象，为空直接抛出异常
        UserInfo userInfo = this.findByPhone(username);
        if (userInfo == null){
            throw new BusinessException(500401, "用户名或密码错误");
        }
        //对前端传入的密码进行加密
        String encryptPassword = Md5Utils.getMD5(password + username);
        //校验前端密码和数据库密码是否一致，不一致直接抛出异常
        if (!encryptPassword.equalsIgnoreCase(userInfo.getPassword())){
            throw new BusinessException(500401, "用户名或密码错误");
        }

        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(userInfo, loginUser);


        // 当前时间
        long now = System.currentTimeMillis();
        loginUser.setLoginTime(now);
        //过期时间
        Long expireTime = now + (jwtProperties.getExpireTime() * LoginUser.MINUTES_MILLISECONDS);
        loginUser.setExpireTime(expireTime);

        //生成一个用户uuid 是用户存入redis的唯一标识
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        UserRedisKeyPrefix loginInfoString = UserRedisKeyPrefix.USER_LOGIN_INFO_STRING;//获取前缀
        loginInfoString.setTimeout(Math.toIntExact(expireTime));
        loginInfoString.setUnit(TimeUnit.MILLISECONDS);
        redisCache.setCacheObject(loginInfoString, loginUser, uuid);

        //使用jwt生成Token，往jwt中存入用户基础信息
        Map<String, Object> payload = new HashMap<>();
        payload.put(LoginUser.LOGIN_USER_REDIS_UUID, uuid);

        String jwtToken = Jwts.builder()
                .addClaims(payload)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
        //构建map对象，存入token以及用户对象，返回给前端
        payload.clear();
        payload.put("token", jwtToken);
        payload.put("user", loginUser);
        return payload;
    }

    @Override
    public UserInfoDTO getDtoById(Long id) {
        UserInfo userInfo = super.getById(id);
        if (userInfo != null){
            UserInfoDTO dto = new UserInfoDTO();
            BeanUtils.copyProperties(userInfo, dto);
            return dto;
        }
        return null;
    }

    /**
     * 查询用户收藏文章 id 集合
     * @param userId
     * @return
     */
    @Override
    public List<Long> getFavorStrategyIdList(Long userId) {
        List<Long> list = getBaseMapper().selectFavorStrategyIdList(userId);
        return list;
    }

    @Override
    public boolean favoriteStrategy(Long sid) {
        // 获取当前登录用户
        LoginUser user = AuthenticationUtils.getUser();
        // 获取当前用户收藏的文章列表
        List<Long> list = this.getFavorStrategyIdList(user.getId());
        // 判断当前用户是否已经收藏过该文章
        if (list.contains(sid)){
            // 收藏过，取消收藏，数量-1
            getBaseMapper().deleteFavoriteStrategy(user.getId(), sid);
            redisCache.hashIncrement(UserRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP, "favornum", -1, sid+"");
            return false;
        }

        // 没收藏，收藏，数量+1
        getBaseMapper().insertFavoriteStrategy(user.getId(), sid);
        redisCache.hashIncrement(UserRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP, "favornum", 1, sid+"");
        return true;
    }

    /**
     * 构建用户对象
     * @param req
     * @return
     */
    private UserInfo buildUserInfo(RegisterRequest req) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(req, userInfo);
        userInfo.setInfo("流汗");
        userInfo.setHeadImgUrl("/images/default.jpg");
        userInfo.setState(UserInfo.STATE_NORMAL);
        return userInfo;
    }
}
