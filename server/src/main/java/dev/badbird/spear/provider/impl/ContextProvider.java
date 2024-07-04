package dev.badbird.spear.provider.impl;

import dev.badbird.spear.http.RouteInfo;
import dev.badbird.spear.provider.SpearProvider;
import io.javalin.http.Context;

import java.lang.reflect.Parameter;

public class ContextProvider implements SpearProvider<Context> {

    @Override
    public Context provide(Context ctx, RouteInfo routeInfo, Parameter param) {
        return ctx;
    }
}
