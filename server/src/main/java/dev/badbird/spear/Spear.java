package dev.badbird.spear;

import com.google.gson.Gson;
import dev.badbird.spear.http.*;
import dev.badbird.spear.provider.SpearProvider;
import dev.badbird.spear.provider.impl.ContextProvider;
import dev.badbird.spear.provider.impl.StringProvider;
import dev.badbird.spear.rest.APIRequest;
import dev.badbird.spear.rest.HTTPMethod;
import dev.badbird.spear.rest.Route;
import dev.badbird.spear.result.ReturnHandler;
import dev.badbird.spear.result.impl.StringReturnHandler;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.Data;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Data
public class Spear {
    private final Javalin app;
    private Gson gson = new Gson();
    private Map<Class<?>, SpearProvider<?>> providers = new HashMap<>();
    private Map<Class<?>, ReturnHandler<?>> returnHandlers = new HashMap<>();

    public Spear register(SpearHandler handler) {
        String prefix = "/";
        Route route = handler.getRoute();
        if (route != null) {
            prefix = route.value();
        }
        // pre-process prefix
        if (prefix.endsWith("/")) {
            prefix = prefix.substring(0, prefix.length() - 1);
        }
        if (!prefix.startsWith("/")) {
            prefix = "/" + prefix;
        }
        if (handler instanceof APIHandler<?, ?> apiHandler) {
            registerAPIHandler(apiHandler);
        }
        for (Method declaredMethod : handler.getClass().getDeclaredMethods()) {
            registerMethod(declaredMethod, prefix, handler);
        }
        return this;
    }

    public Spear register(SpearHandler handler, SpearHandler... handlers) {
        register(handler);
        if (handlers == null) {
            return this;
        }
        for (SpearHandler h : handlers) {
            register(h);
        }
        return this;
    }

    public Spear registerPackage(String packageName) {
        for (Class<? extends SpearHandler> aClass : new Reflections(packageName).getSubTypesOf(SpearHandler.class)) {
            try {
                register(aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public Spear setGson(Gson gson) {
        this.gson = gson;
        return this;
    }

    @SneakyThrows
    private void registerAPIHandler(APIHandler<?, ?> handler) {
        Route route = handler.getRoute();
        String path = route.value();
        Method method = handler.getClass().getDeclaredMethod("handle", APIRequest.class, Context.class);
        RouteInfo routeInfo = new RouteInfo(route, path, handler, method);
        Handler httpHandler = new SpearHttpHandler(routeInfo, this);
        for (HTTPMethod httpMethod : route.method()) {
            HttpMethodTranslator.getMethodMap().get(httpMethod).apply(app, path, httpHandler);
        }
    }

    private void registerMethod(Method method, String prefix, SpearHandler handler) {
        if (method.isAnnotationPresent(Route.class)) {
            Route route = method.getAnnotation(Route.class);
            String path = prefix + "/" + route.value().replace("//", "/"); // clean up path
            RouteInfo routeInfo = new RouteInfo(route, path, handler, method);
            Handler httpHandler = new SpearHttpHandler(routeInfo, this);
            for (HTTPMethod httpMethod : route.method()) {
                HttpMethodTranslator.getMethodMap().get(httpMethod).apply(app, path, httpHandler);
            }
        }
    }

    public Spear start(int port) {
        app.start(port);
        new SpearCommons(gson, "", null);
        return this;
    }

    public <T> Spear registerProvider(Class<T> clazz, SpearProvider<T> provider) {
        providers.put(clazz, provider);
        return this;
    }

    public <T> Spear registerReturnHandler(Class<T> clazz, ReturnHandler<T> returnHandler) {
        returnHandlers.put(clazz, returnHandler);
        return this;
    }

    public static Spear bind(Javalin app) {
        return new Spear(app)
                .registerProvider(Context.class, new ContextProvider())
                .registerProvider(String.class, new StringProvider())
                .registerReturnHandler(String.class, new StringReturnHandler());

    }
}