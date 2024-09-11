package com.song.netty.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD}) //Annotation所修饰的对象范围
@Retention(RetentionPolicy.RUNTIME) //注解生命周期:注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在
@Documented
public @interface RemoteInvoke {

}
