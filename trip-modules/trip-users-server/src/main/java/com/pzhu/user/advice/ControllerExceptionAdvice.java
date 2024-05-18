package com.pzhu.user.advice;

import com.pzhu.core.exception.BusinessException;
import com.pzhu.core.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public R<?> commonException(Exception e){
        log.error("[统一异常处理] 拦截其他异常", e);
        return R.defaultError();
    }

    @ExceptionHandler(BusinessException.class)
    public R<?> commonException(BusinessException e){
        if (log.isDebugEnabled()){//这个方法是打印红色报错的内容，打印内容很多
            log.warn("[统一异常处理] 拦截业务异常", e);
        }else {
            log.warn("[统一异常处理] 拦截业务异常， code = {}, msg = {}", e.getCode(), e.getMessage());
        }
        return R.error(e.getCode(),e.getMessage());
    }
}
