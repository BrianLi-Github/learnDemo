package com.example.demo.spring.main;

import com.example.demo.spring.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

// ApplicationContextAware会为Component组件调用setApplicationContext方法；
@Component
public class SpringContextHolder implements ApplicationContextAware {

    /**
     * 获取自定义的RPC Service，装配到map中
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> rpcBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        rpcBeanMap.forEach((key, bean) -> {
            System.out.println("获取到自定义RPC Service: " + key);
            System.out.println("该Service注解上的value是：" + bean.getClass().getAnnotation(RpcService.class));

            //模拟收到客户端请求，反射被注解类，并调用指定方法
            try {
                Method method = bean.getClass().getMethod("doSomething", new Class[]{String.class});
                Object result = method.invoke(bean, "rpc demo");
                System.out.println("返回结果： " + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
