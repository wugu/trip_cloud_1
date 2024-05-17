package com.pzhu.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.core.utils.Md5Utils;
import com.pzhu.redis.utils.RedisCache;
import com.pzhu.user.mapper.UserInfoMapper;
import com.pzhu.user.service.UserInfoService;
import com.pzhu.user.domain.UserInfo;
import com.pzhu.user.vo.RegisterRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    private final RedisCache redisCache;

    public UserServiceImpl(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

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
            throw new RuntimeException("手机号已经存在，请登录");
        }
        //从redis中取得验证码，与前端传来的验证码进行校验
        String code = redisCache.getCacheObject("USERS:REGISTER:VERIFY_CODE" + req.getPhone());
        if (!req.getVerifyCode().equalsIgnoreCase(code)){
            throw new RuntimeException("验证码错误");
        }
        //将验证码从redis中删除
        redisCache.deleteObject("USERS:REGISTER:VERIFY_CODE" + req.getPhone());
        //创建用户对象
        userInfo = this.buildUserInfo(req);
        //对密码进行加密
        //加盐+散列（hash）次数  增加密码的安全性
        String encryptPassword = Md5Utils.getMD5(userInfo.getPassword() + userInfo.getPhone());
        userInfo.setPassword(encryptPassword);
        //保存到数据库中   mybatisPlus
        super.save(userInfo);
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
