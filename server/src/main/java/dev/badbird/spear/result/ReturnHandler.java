package dev.badbird.spear.result;

import dev.badbird.spear.http.RouteInfo;
import io.javalin.http.Context;

public interface ReturnHandler<T> {
    void handle(T obj, Context context, RouteInfo info);
    default void handleObj(Object obj, Context context, RouteInfo info){
        handle((T) obj, context, info);
    }
}
