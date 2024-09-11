package com.song.netty.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD}) //Annotation所修饰的对象范围
@Retention(RetentionPolicy.RUNTIME) //注解生命周期:注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在
@Documented
@Component
public @interface Remote { //和Controller注解保持一致
    String value() default "";
}
