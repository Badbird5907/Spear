package dev.badbird.spear.provider;

import dev.badbird.spear.http.RouteInfo;
import io.javalin.http.Context;

import java.lang.reflect.Parameter;

public interface SpearProvider<T> {
    T provide(Context ctx, RouteInfo routeInfo, Parameter param);
}
