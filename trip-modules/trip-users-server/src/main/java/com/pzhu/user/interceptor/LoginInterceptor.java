package com.pzhu.user.interceptor;

import com.pzhu.core.exception.BusinessException;
import com.pzhu.redis.utils.RedisCache;
import com.pzhu.user.redis.key.UserRedisKeyPrefix;
import com.pzhu.user.vo.LoginUser;
import com.sun.corba.se.impl.resolver.SplitLocalResolverImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final RedisCache redisCache;

    public LoginInterceptor(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从请求头中拿到token
        String token = request.getHeader("token");
        //基于jwt sdk 解析token，解析失败抛出异常
        try {
            Jws<Claims> jwt = Jwts.parser()
                    .setSigningKey("秘钥")
                    .parseClaimsJws(token);
            //获取token中登录时间数据，判断是否已经过期
            Claims claims = jwt.getBody();
            String uuid = (String) claims.get("uuid");

            //从redis中获取数据，拿到说明没有过期，拿不到说明已经过期
            String userLoginKey = UserRedisKeyPrefix.USER_LOGIN_INFO_STRING.fullKey(uuid);
            LoginUser loginUser = redisCache.getCacheObject(userLoginKey);
            Long loginTime;

            if (loginUser == null){
                //过期，抛出异常
                throw new BusinessException(401, "token 已失效");
            }else if ((loginUser.getExpireTime() - (loginTime = System.currentTimeMillis())) <= 20 * 60 * 1000){
                //如果用户剩余时间少于20min，就刷新用户的过期时间
                loginUser.setLoginTime(loginTime);
                Long expireTime = loginTime + (30 * 60 * 1000);
                loginUser.setExpireTime(expireTime);//过期时间
                //重新计算过期时间，再次设置到redis中， 覆盖原来redis中的对象
                redisCache.setCacheObject(userLoginKey, loginUser, expireTime, TimeUnit.MINUTES);
            }
        }catch (Exception e){

            throw new BusinessException("用户未认证");
        }
        //其他情况返回true
        return true;
    }
}
