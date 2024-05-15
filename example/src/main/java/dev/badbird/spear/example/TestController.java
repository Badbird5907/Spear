package dev.badbird.spear.example;

import dev.badbird.spear.annotation.PathParam;
import dev.badbird.spear.annotation.Route;
import dev.badbird.spear.http.HttpMethod;
import dev.badbird.spear.http.SpearHandler;
import io.javalin.http.Context;

@Route("/test")
public class TestController implements SpearHandler {
    @Route(value = "/", method = HttpMethod.GET) // hit localhost:8080/test/
    public void test(Context ctx) {
        System.out.println("Hello, World!");
        ctx.result("HW1");
    }
    @Route(value = "/{pathparam}", method = HttpMethod.GET) // hit localhost:8080/test/paramnamehere
    public String test2(Context ctx, @PathParam("pathparam") String param) {
        return param;
    }
}