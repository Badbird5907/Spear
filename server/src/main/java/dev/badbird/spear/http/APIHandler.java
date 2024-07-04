package dev.badbird.spear.http;

import com.google.gson.JsonObject;
import dev.badbird.spear.SpearCommons;
import dev.badbird.spear.rest.APIRequest;
import dev.badbird.spear.rest.Route;
import io.javalin.http.Context;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface APIHandler<R, T extends APIRequest<R>> extends SpearHandler {
    R handle(T request, Context ctx);

    default Class<T> getRequestClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[1];
    }

    @Override
    default Route getRoute() {
        return getRequestClass().getAnnotation(Route.class);
    }

    default T reconstructRequest(Context ctx) {
        Route route = getRoute();
        Route.Flags[] flags = route.flags();
        boolean checkQuery = !route.method()[0].isHasBody() && (flags.length == 0 || Arrays.stream(flags).anyMatch(flag -> flag == Route.Flags.VARIABLES_IN_QUERY));
        if (checkQuery) {
            Map<String, List<String>> map = ctx.queryParamMap();
            JsonObject jo = new JsonObject();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                jo.addProperty(entry.getKey(), entry.getValue().get(0));
            }
            return SpearCommons.get().getGson().fromJson(jo, getRequestClass());
        }
        String body = ctx.body();
        if (body.isEmpty()) {
            body = "{}"; // fuck you
        }
        return SpearCommons.get().getGson().fromJson(body, getRequestClass());
    }
}
