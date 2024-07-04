package dev.badbird.spear.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Route {
    /**
     * The endpoint of the request
     * @return The endpoint
     */
    String value();
    HTTPMethod[] method() default HTTPMethod.GET;
    Flags[] flags() default {};

    enum Flags {
        /**
         * Variables are injected into the query string
         * Example: /api/123?var1=1&var2=true
         * See {@link APIRequest#serializeToQuery()}
         */
        VARIABLES_IN_QUERY,
        /**
         * Variables are sent as a JSON body using Gson
         */
        VARIABLES_IN_BODY,
        /**
         * Variables are ignored
         */
        IGNORE_VARIABLES;
    }
}
