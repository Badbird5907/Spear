package dev.badbird.spear.test;

import dev.badbird.spear.annotation.PathParam;
import dev.badbird.spear.annotation.Route;
import dev.badbird.spear.http.HttpMethod;
import dev.badbird.spear.http.SpearHandler;
import io.javalin.http.Context;

@Route("/test")
public class TestController implements SpearHandler {
    @Route(value = "/", method = HttpMethod.GET)
    public void test(Context ctx) {
        System.out.println("Hello, World!");
        ctx.result("HW1");
    }
    @Route(value = "/{test}", method = HttpMethod.GET)
    public String test2(Context ctx, @PathParam("test") String test) {
        return test;
    }
}
