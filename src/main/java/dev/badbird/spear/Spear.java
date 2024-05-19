package dev.badbird.spear;

import com.google.gson.Gson;
import dev.badbird.spear.annotation.Route;
import dev.badbird.spear.http.HttpMethod;
import dev.badbird.spear.http.RouteInfo;
import dev.badbird.spear.http.SpearHandler;
import dev.badbird.spear.http.SpearHttpHandler;
import dev.badbird.spear.provider.SpearProvider;
import dev.badbird.spear.provider.impl.ContextProvider;
import dev.badbird.spear.provider.impl.StringProvider;
import dev.badbird.spear.result.ReturnHandler;
import dev.badbird.spear.result.impl.StringReturnHandler;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.Data;
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
        if (handler.getClass().isAnnotationPresent(Route.class)) {
            Route route = handler.getClass().getAnnotation(Route.class);
            prefix = route.value();
        }
        // pre-process prefix
        if (prefix.endsWith("/")) {
            prefix = prefix.substring(0, prefix.length() - 1);
        }
        if (!prefix.startsWith("/")) {
            prefix = "/" + prefix;
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

    private void registerMethod(Method method, String prefix, SpearHandler handler) {
        if (method.isAnnotationPresent(Route.class)) {
            Route route = method.getAnnotation(Route.class);
            String path = prefix + "/" + route.value().replace("//", "/"); // clean up path
            RouteInfo routeInfo = new RouteInfo(route, path, handler, method);
            Handler httpHandler = new SpearHttpHandler(routeInfo, this);
            for (HttpMethod httpMethod : route.method()) {
                httpMethod.apply(app, path, httpHandler);
            }
        }
    }

    public Spear start(int port) {
        app.start(port);
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