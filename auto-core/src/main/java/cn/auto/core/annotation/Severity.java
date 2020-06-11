package cn.auto.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by chenmeng on 2016/11/23.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Severity {
    enum Level {
        BLOCKER,
        CRITICAL,
        MAJOR
    }
    Level level() default Level.MAJOR;
}
