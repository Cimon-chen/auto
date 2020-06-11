package cn.auto.core.annotation;

/**
 * Created by chenmeng on 2016/4/26.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
public @interface Task {
    String[] taskName() default {};
    String[] paras() default {};
}
