package dev.badbird.spear.rest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.badbird.spear.SpearCommons;
import dev.badbird.spear.util.TemplateStringParser;
import lombok.SneakyThrows;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface APIRequest<T> {
    OkHttpClient CLIENT = new OkHttpClient();
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    default Type getResponseType() {
        return ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }

    default HTTPMethod getHttpMethod() {
        if (getClass().isAnnotationPresent(Route.class)) {
            return getClass().getAnnotation(Route.class).method()[0];
        }
        // if there are fields, assume it's a POST request
        if (getClass().getDeclaredFields().length > 1) {
            return HTTPMethod.POST;
        }
        return HTTPMethod.GET;
    }

    default Route.Flags[] getReqFlags() {
        if (getClass().isAnnotationPresent(Route.class)) {
            return getClass().getAnnotation(Route.class).flags();
        }
        return new Route.Flags[0];
    }

    @SneakyThrows
    default String serializeToQuery() {
        // only serialize primitive types
        StringBuilder builder = new StringBuilder();
        /*
        for (java.lang.reflect.Field field : getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object o = field.get(this);
                if (o == null) continue;
                if (o instanceof String) {
                    builder.append(field.getName()).append("=").append(o).append("&");
                } else if (o instanceof Number) {
                    builder.append(field.getName()).append("=").append(o).append("&");
                } else if (o instanceof Boolean) {
                    builder.append(field.getName()).append("=").append(o).append("&");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
         */
        JsonObject obj = SpearCommons.get().getGson().toJsonTree(this).getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) { // TODO: figure out how to serialize this without losing type info
            String value = "";
            if (entry.getValue().isJsonPrimitive()) {
                JsonPrimitive primitive = entry.getValue().getAsJsonPrimitive();
                if (primitive.isString()) {
                    value = primitive.getAsString();
                } else if (primitive.isBoolean()) {
                    value = Boolean.toString(primitive.getAsBoolean());
                } else if (primitive.isNumber()) {
                    value = primitive.getAsNumber().toString();
                }
            } else {
                value = SpearCommons.get().getGson().toJson(entry.getValue());
            }
            String s = URLEncoder.encode(value, StandardCharsets.UTF_8);
            builder.append(entry.getKey()).append("=")
                    .append(s)
                    .append("&");
        }
        String query = builder.toString();
        if (query.endsWith("&")) {
            query = query.substring(0, query.length() - 1);
        }
        return query;
    }

    default Request.Builder addData(Request.Builder builder) {
        Route.Flags[] flags1 = getReqFlags();
        List<Route.Flags> flags = List.of(flags1);
        HTTPMethod type = getHttpMethod();
        if (flags.contains(Route.Flags.IGNORE_VARIABLES)) return builder;
        if (!type.isHasBody()) {
            if (flags.contains(Route.Flags.VARIABLES_IN_BODY)) {
                throw new RuntimeException("Cannot use VARIABLES_IN_BODY flag with " + type.name() + " request");
            }
            if (flags.isEmpty() || flags.contains(Route.Flags.VARIABLES_IN_QUERY)) {
                return builder.url(builder.build().url() + "?" + serializeToQuery());
            }
        } else {
            if (flags.isEmpty() || flags.contains(Route.Flags.VARIABLES_IN_BODY)) {
                return builder.post(RequestBody.create(SpearCommons.get().getGson().toJson(this), JSON));
            } else if (flags.contains(Route.Flags.VARIABLES_IN_QUERY)) {
                return builder.url(builder.build().url() + "?" + serializeToQuery());
            }
        }
        return builder;
    }

    @SneakyThrows
    default CompletableFuture<T> send() {
        CompletableFuture<T> future = new CompletableFuture<>();
        try {
            String endpoint = getClass().getAnnotation(Route.class).value();
            if (endpoint.startsWith("/")) endpoint = endpoint.substring(1);
            String notSoFinalEndpoint = endpoint;

            Map<String, Object> variables = new HashMap<>();
            List<String> pathVariables = TemplateStringParser.parse(notSoFinalEndpoint);
            for (String pathVariable : pathVariables) {
                if (pathVariable.contains(".")) {
                    String[] split = pathVariable.split(".");
                    Field field = getClass().getDeclaredField(split[0]);
                    field.setAccessible(true);
                    Object o = field.get(this);
                    // traverse the object
                    for (int i = 1; i < split.length; i++) {
                        field = o.getClass().getDeclaredField(split[i]);
                        field.setAccessible(true);
                        o = field.get(o);
                    }
                    variables.put(pathVariable, o);
                } else {
                    String varName = pathVariable.substring(1, pathVariable.length() - 1);
                    Field field = getClass().getDeclaredField(varName);
                    field.setAccessible(true);
                    variables.put(varName, field.get(this));
                }
            }

            String finalEndpoint = TemplateStringParser.mapFields(variables, notSoFinalEndpoint); // GOD IS DEAD AND WE KILLED HIM
            Request.Builder builder = new Request.Builder().url(SpearCommons.get().getBaseUrl() + finalEndpoint);
            Request.Builder a = addData(builder);
            if (a != null) builder = a;
            Request.Builder finalBuilder = SpearCommons.get().getRequestMutator().apply(builder);
            CLIENT.newCall(finalBuilder.build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    future.completeExceptionally(e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        future.completeExceptionally(new RuntimeException("Unexpected code " + response.code()));
                    }
                    String body = response.body().string();
                    System.out.println(body);
                    T t = SpearCommons.get().getGson().fromJson(body, getResponseType());
                    response.close();
                    future.complete(t);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            future.completeExceptionally(e);
        }
        return future;
    }
}
