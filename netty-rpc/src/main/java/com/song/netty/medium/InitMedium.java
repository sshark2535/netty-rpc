package com.song.netty.medium;

import com.song.netty.annotation.Remote;
import com.song.netty.annotation.RemoteInvoke;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Map;

@Component
public class InitMedium implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        if (o.getClass().isAnnotationPresent(Remote.class)){ //
            System.out.println(o.getClass().getName());
            Method[] methods = o.getClass().getDeclaredMethods();
            for(Method m : methods){
                Map<String, BeanMethod> beanMethodMap = Media.beanMethodMap;
                BeanMethod beanMethod = new BeanMethod();
                beanMethod.setBean(o);
                beanMethod.setMethod(m);
                String key = o.getClass().getInterfaces()[0].getName()+"."+m.getName(); //UserRemoteImpl
                beanMethodMap.put(key,beanMethod);
                System.out.println(key);
            }
        }
        return o;
    }
}
