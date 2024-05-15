package dev.badbird.spear.http;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.router.JavalinDefaultRoutingApi;

public enum HttpMethod {
    GET(JavalinDefaultRoutingApi::get),
    POST(JavalinDefaultRoutingApi::post),
    PUT(JavalinDefaultRoutingApi::put),
    DELETE(JavalinDefaultRoutingApi::delete),
    PATCH(JavalinDefaultRoutingApi::patch);

    private ApplyFunction applyFn;

    HttpMethod(ApplyFunction applyFn) {
        this.applyFn = applyFn;
    }

    public Javalin apply(Javalin app, String path, Handler handler) {
        return applyFn.apply(app, path, handler);
    }

    private interface ApplyFunction {
        Javalin apply(Javalin app, String path, Handler handler);
    }
}
