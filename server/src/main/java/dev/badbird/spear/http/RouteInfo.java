package dev.badbird.spear.http;

import dev.badbird.spear.Spear;
import dev.badbird.spear.annotation.RequestBody;
import dev.badbird.spear.provider.SpearProvider;
import dev.badbird.spear.rest.Route;
import dev.badbird.spear.validator.impl.JakartaValidator;
import io.javalin.http.Context;
import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@Data
public class RouteInfo {
    private final Route route;
    private final String path;
    private final SpearHandler handler;
    private final Method method;

    public List<Object> buildParameters(Spear spear, Context ctx) {
        List<Object> parameters = new ArrayList<>();
        if (isAPIHandler()) {
            APIHandler<?, ?> apiHandler = (APIHandler<?, ?>) handler;
            try {
                parameters.add(apiHandler.reconstructRequest(ctx));
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            parameters.add(ctx);
        } else {
            for (Parameter parameter : method.getParameters()) {
                SpearProvider<?> spearProvider = spear.getProviders().get(parameter.getType());
                if (spearProvider == null) {
                    if (parameter.isAnnotationPresent(RequestBody.class)) {
                        Object o = spear.getGson().fromJson(ctx.body(), parameter.getType());
                        JakartaValidator.INSTANCE.validate(o);
                        parameters.add(o);
                    } else {
                        throw new RuntimeException("No provider found for " + parameter.getType().getName());
                    }
                } else {
                    parameters.add(spearProvider.provide(ctx, this, parameter));
                }
            }
        }
        return parameters;
    }

    public boolean isAPIHandler() {
        return handler instanceof APIHandler<?, ?>;
    }
}
