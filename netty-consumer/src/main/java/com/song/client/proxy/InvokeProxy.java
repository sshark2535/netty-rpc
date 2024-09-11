package com.song.client.proxy;

import com.song.client.annotation.RemoteInvoke;
import com.song.client.core.TCPClient;
import com.song.client.param.ClientRequest;
import com.song.client.param.Response;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class InvokeProxy implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields(); // 获取包括父类在内的所有字段
        for(Field field:fields){
            if(field.isAnnotationPresent(RemoteInvoke.class)){ // 判断RemoteInvoke注解是否在字段上
                field.setAccessible(true);

                final Map<Method,Class> methodClassMap = new HashMap<Method, Class>();
                putMethodClass(methodClassMap, field);

                Enhancer enhancer = new Enhancer();
                enhancer.setInterfaces(new Class[]{field.getType()});
                enhancer.setCallback(new MethodInterceptor() {
                    //进行拦截
                    @Override
                    public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                        ClientRequest request = new ClientRequest();
                        request.setCommand(methodClassMap.get(method).getName()+"."+method.getName());
                        request.setContext(args[0]);
                        Response response = TCPClient.send(request);
                        return response;
                    }
                });
                try {
                    field.set(bean, enhancer.create());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }

    private void putMethodClass(Map<Method, Class> methodClassMap, Field field) {
        Method[] methods = field.getType().getDeclaredMethods();
        for(Method m : methods){
            methodClassMap.put(m, field.getType());
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return null;
    }
}
