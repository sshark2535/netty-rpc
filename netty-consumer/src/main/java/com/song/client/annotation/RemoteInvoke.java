package com.song.client.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD}) //Annotation所修饰的对象范围
@Retention(RetentionPolicy.RUNTIME) //注解生命周期:注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在
@Documented
@Component
public @interface RemoteInvoke {

}
