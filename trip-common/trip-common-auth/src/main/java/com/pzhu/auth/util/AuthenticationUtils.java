package com.pzhu.auth.util;

import com.pzhu.auth.config.JwtProperties;
import com.pzhu.core.exception.BusinessException;
import com.pzhu.redis.utils.RedisCache;
import com.pzhu.user.redis.key.UserRedisKeyPrefix;
import com.pzhu.user.vo.LoginUser;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
abstract public class AuthenticationUtils {

    private static final ThreadLocal<LoginUser> USER_HOLDER = new ThreadLocal<>();

    public static HttpServletRequest getRequest() {
        // 只要是在 Spring MVC 环境中运行，就不可能为空
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr == null){
            throw new BusinessException("该方法只能在 Spring MVC 环境下调用");
        }
        return attr.getRequest();
    }

    public static String getToken(){ // 拿到请求头中的 token
        return getRequest().getHeader(LoginUser.TOKEN_HEADER);
    }

    public static LoginUser getUser(){
        LoginUser cacheUser = USER_HOLDER.get();
        if (cacheUser != null){
            return cacheUser; // 缓存到 jvm 中
        }

        String token = getToken();
        if (StringUtils.isEmpty(token)){
            return null;
        }

        JwtProperties jwtProperties = SpringContextUtil.getBean(JwtProperties.class);
        RedisCache redisCache = SpringContextUtil.getBean(RedisCache.class);

        try { // 不用担心 jwt 有效时间
            Jws<Claims> jwt = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token);
            //获取token中登录时间数据，判断是否已经过期
            Claims claims = jwt.getBody();
            String uuid = (String) claims.get(LoginUser.LOGIN_USER_REDIS_UUID);

            //从redis中获取数据，拿到说明没有过期，拿不到说明已经过期
            String userLoginKey = UserRedisKeyPrefix.USER_LOGIN_INFO_STRING.fullKey(uuid);
            LoginUser user = redisCache.getCacheObject(userLoginKey);// 返回用户对象
            // 将第一次查询用户信息保存起来
            USER_HOLDER.set(user);
            return user;
        } catch (Exception e) {
            log.warn("[认证工具] 获取用户信息失败", e);
        }
        return null;
    }

    // 在登录拦截器中执行线程结束，清除缓存（jvm 中的 user
    public static void removeUser() {
        USER_HOLDER.remove();
    }
}
