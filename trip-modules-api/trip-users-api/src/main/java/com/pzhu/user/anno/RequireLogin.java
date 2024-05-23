package com.pzhu.user.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能： 标识方法需要登录才能访问，可以贴在类或方法上
 * 贴在类上，表示类中所有接口都需要登录才能访问
 */
@Target({ElementType.TYPE, ElementType.METHOD})//表示可以贴在类上，方法上
@Retention(RetentionPolicy.RUNTIME)//存活时期：运行时期
public @interface RequireLogin {

}
