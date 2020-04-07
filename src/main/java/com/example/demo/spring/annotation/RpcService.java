package com.example.demo.spring.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识某个service是否是自定义的
 */
@Target({ElementType.TYPE})  // 注解用在接口上
@Retention(RetentionPolicy.RUNTIME) //JVM在运行时也可以保留该注解，所以运行时可以通过反射机制读取注解的信息
@Component   // 可让spring扫描到这个自定义的Java对象
public @interface RpcService {
    String value();
}
