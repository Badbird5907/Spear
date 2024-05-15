package dev.badbird.spear.annotation;

import dev.badbird.spear.http.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Route {
    String value();
    HttpMethod[] method() default HttpMethod.GET;
}
