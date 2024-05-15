package dev.badbird.spear.http;

import dev.badbird.spear.Spear;
import dev.badbird.spear.annotation.Route;
import dev.badbird.spear.provider.SpearProvider;
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
        for (Parameter parameter : method.getParameters()) {
            SpearProvider<?> spearProvider = spear.getProviders().get(parameter.getType());
            if (spearProvider == null) {
                throw new RuntimeException("No provider found for " + parameter.getType().getName());
            }
            parameters.add(spearProvider.provide(ctx, this, parameter));
        }
        return parameters;
    }
}
