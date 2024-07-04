package dev.badbird.spear;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import okhttp3.Request;

import java.util.function.Function;

@Data
public class SpearCommons {
    private static SpearCommons instance;
    public SpearCommons(Gson gson, String baseUrl, Function<Request.Builder, Request.Builder> requestMutator) {
        this.gson = gson;
        this.requestMutator = requestMutator;
        // ensure baseUrl ends with a /
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        this.baseUrl = baseUrl;


        instance = this;
    }
    private Gson gson;
    private String baseUrl;
    private Function<Request.Builder, Request.Builder> requestMutator;

    public static SpearCommons get() {
        return instance;
    }
}
