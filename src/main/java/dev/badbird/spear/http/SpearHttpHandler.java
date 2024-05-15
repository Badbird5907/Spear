package dev.badbird.spear.http;

import dev.badbird.spear.Spear;
import dev.badbird.spear.result.ReturnHandler;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class SpearHttpHandler implements Handler {
    private final RouteInfo routeInfo;
    private final Spear spear;

    @Override
    public void handle(@NotNull Context context) {
        try {
            List<Object> parameters = routeInfo.buildParameters(spear, context);
            Object result = routeInfo.getMethod().invoke(routeInfo.getHandler(), parameters.toArray());
            if (result == null) {
                return;
            }
            ReturnHandler<?> returnHandler = spear.getReturnHandlers().get(result.getClass());
            if (returnHandler != null) {
                returnHandler.handleObj(result, context, routeInfo);
            } else {
                context.json(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
