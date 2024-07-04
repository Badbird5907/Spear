package dev.badbird.spear.http;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.badbird.spear.Spear;
import dev.badbird.spear.result.ReturnHandler;
import dev.badbird.spear.validator.ValidatorException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
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
        } catch (ValidatorException vE) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", true);
            jsonObject.addProperty("message", vE.getMessage());
            JsonArray jsonArray = new JsonArray();
            for (String message : vE.getMessages()) {
                jsonArray.add(message);
            }
            jsonObject.add("validationMessages", jsonArray);
            jsonObject.addProperty("status", 400);
            context.status(400).result(spear.getGson().toJson(jsonObject)).contentType("application/json");
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
