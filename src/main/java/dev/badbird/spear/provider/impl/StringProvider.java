package dev.badbird.spear.provider.impl;

import dev.badbird.spear.annotation.PathParam;
import dev.badbird.spear.http.RouteInfo;
import dev.badbird.spear.provider.SpearProvider;
import io.javalin.http.Context;

import java.lang.reflect.Parameter;

public class StringProvider implements SpearProvider<String> {
    @Override
    public String provide(Context ctx, RouteInfo routeInfo, Parameter param) {
        if (param.isAnnotationPresent(PathParam.class)) {
            return ctx.pathParam(param.getAnnotation(PathParam.class).value());
        }
        throw new IllegalArgumentException("Don't know how to provide string for parameter: " + param.getName() + " in route: " + routeInfo.getPath());
    }
}
