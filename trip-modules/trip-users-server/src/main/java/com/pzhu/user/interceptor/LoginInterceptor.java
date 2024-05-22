package com.pzhu.user.interceptor;

import com.pzhu.core.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
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
            Long loginTime = (Long) claims.get("loginTime");
            Long expireTime = (Long) claims.get("expireTime");
            long current = System.currentTimeMillis();
            Long min  = (current - loginTime) / 1000 / 60;
            if (min > expireTime){
                //过期，抛出异常
                throw new BusinessException(401, "token 已失效");
            }
        }catch (Exception e){
            throw new BusinessException("用户未认证");
        }
        //其他情况返回true
        return true;
    }
}
