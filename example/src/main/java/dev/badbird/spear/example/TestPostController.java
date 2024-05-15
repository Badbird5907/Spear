package dev.badbird.spear.example;

import dev.badbird.spear.annotation.Route;
import dev.badbird.spear.http.HttpMethod;
import dev.badbird.spear.http.SpearHandler;
import io.javalin.http.Context;

public class TestPostController implements SpearHandler {
    @Route(value = "/postme", method = HttpMethod.POST)
    public void postMe(Context ctx) {
        System.out.println("Received POST request!");
        ctx.result(ctx.body());
    }
}
