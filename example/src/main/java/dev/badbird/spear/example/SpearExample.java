package dev.badbird.spear.example;

import dev.badbird.spear.Spear;
import io.javalin.Javalin;

public class SpearExample {
    public static void main(String[] args) {
        Javalin app = Javalin.create()
                .start(8133);
        Spear.bind(app)
                .register(new TestController(), new TestPostController());
    }
}