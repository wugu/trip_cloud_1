package com.pzhu.auth.inceptor;

import com.pzhu.auth.anno.RequireLogin;
import com.pzhu.auth.config.JwtProperties;
import com.pzhu.auth.util.AuthenticationUtils;
import com.pzhu.core.exception.BusinessException;
import com.pzhu.redis.utils.RedisCache;
import com.pzhu.user.redis.key.UserRedisKeyPrefix;
import com.pzhu.user.vo.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private final RedisCache redisCache;
    private final JwtProperties jwtProperties;

    public LoginInterceptor(RedisCache redisCache, JwtProperties jwtProperties) {
        this.redisCache = redisCache;
        this.jwtProperties = jwtProperties;
    }



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //0.判断一个接口是否需要进行登录拦截
        //0.1 判断handler 是否是 HandlerMethod 实例, 如果不是，直接放行
        if (!(handler instanceof HandlerMethod)){
            // handler => 静态资源
            //handler => CORS 的预请求
            return true;
        }

        //0.2 将handler对象转换成 HandlerMethod 对象
        HandlerMethod hm = (HandlerMethod) handler;
        // 0.3 从 HandlerMethod 对象中获取 Controller 对象
        Class<?> controllerClass = hm.getBeanType();
        //0.4 分别从 Controller 和 HandlerMethod 上获取 @RequireLogin 注解
        RequireLogin classAnno = controllerClass.getAnnotation(RequireLogin.class);// 从类上面拿注解
        RequireLogin methodAnno = hm.getMethodAnnotation(RequireLogin.class);// 从方法上拿
        //0.5 如果一个都拿不到，直接放行
        if (classAnno == null && methodAnno == null){
            return true;
        }


        //从请求头中拿到token
        String token = request.getHeader(LoginUser.TOKEN_HEADER);
        //基于jwt sdk 解析token，解析失败抛出异常
        try {
            Jws<Claims> jwt = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token);
            //获取token中登录时间数据，判断是否已经过期
            Claims claims = jwt.getBody();
            String uuid = (String) claims.get(LoginUser.LOGIN_USER_REDIS_UUID);

            //从redis中获取数据，拿到说明没有过期，拿不到说明已经过期
            String userLoginKey = UserRedisKeyPrefix.USER_LOGIN_INFO_STRING.fullKey(uuid);
            LoginUser loginUser = redisCache.getCacheObject(userLoginKey);
            Long loginTime;

            if (loginUser == null){
                //过期，抛出异常
                throw new BusinessException(401, "token 已失效");
            }else if ((loginUser.getExpireTime() - (loginTime = System.currentTimeMillis())) <= LoginUser.TWENTY_MILLISECONDS){
                //如果用户剩余时间少于20min，就刷新用户的过期时间
                loginUser.setLoginTime(loginTime);
                Long expireTime = loginTime + (jwtProperties.getExpireTime() * LoginUser.MINUTES_MILLISECONDS);
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

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 请求执行完成以后准备相应之前执行

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求执行完成以后准备相应之前执行
        // 线程即将完成本次请求，先将当前线程空间内存储的数据清除掉
        AuthenticationUtils.removeUser();

    }
}
