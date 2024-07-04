package dev.badbird.spear.result.impl;

import dev.badbird.spear.http.RouteInfo;
import dev.badbird.spear.result.ReturnHandler;
import io.javalin.http.Context;

public class StringReturnHandler implements ReturnHandler<String> {
    @Override
    public void handle(String obj, Context context, RouteInfo info) {
        context.result(obj);
    }
}
