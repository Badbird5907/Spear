package dev.badbird.spear.test;

import dev.badbird.spear.Spear;
import io.javalin.Javalin;

public class TestMain {
    public static void main(String[] args) {
        Javalin app = Javalin.create()
                .start(8133);
        Spear.bind(app)
                .register(new TestController());
    }
}
