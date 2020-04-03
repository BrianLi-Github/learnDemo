package com.example.demo.proxy.main;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyBoss {

    /**
     * 实现方法的代理
     * @param discountCoupon
     * @param implementClass
     * @param <T>
     * @return
     */
    public static <T> T getProxy(final int discountCoupon, Class<?> implementClass) {
        return (T) Proxy.newProxyInstance(implementClass.getClassLoader(), implementClass.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Integer returnValue = (Integer) method.invoke(implementClass.newInstance(), args);
                return returnValue - discountCoupon;
            }
        });
    }
}
