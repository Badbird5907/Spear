package dev.badbird.spear.example.server;

import dev.badbird.spear.Spear;
import io.javalin.Javalin;
import io.javalin.json.JavalinGson;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(conf -> conf.jsonMapper(new JavalinGson()));
        Spear.bind(app).register(new TestAPIHandler()).start(8089);
        System.out.println("Server started on port 8089");
    }
}