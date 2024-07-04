package dev.badbird.spear.http;

import dev.badbird.spear.rest.HTTPMethod;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.router.JavalinDefaultRoutingApi;
import lombok.Getter;

import java.util.Map;

public class HttpMethodTranslator {
    public interface ApplyFunction {
        Javalin apply(Javalin app, String path, Handler handler);
    }
    @Getter
    private static final Map<HTTPMethod, ApplyFunction> methodMap = Map.of(
            HTTPMethod.GET, JavalinDefaultRoutingApi::get,
            HTTPMethod.POST, JavalinDefaultRoutingApi::post,
            HTTPMethod.PUT, JavalinDefaultRoutingApi::put,
            HTTPMethod.DELETE, JavalinDefaultRoutingApi::delete,
            HTTPMethod.PATCH, JavalinDefaultRoutingApi::patch
    );
}
