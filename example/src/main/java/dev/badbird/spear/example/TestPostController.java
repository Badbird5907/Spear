package dev.badbird.spear.example;

import dev.badbird.spear.annotation.RequestBody;
import dev.badbird.spear.annotation.Route;
import dev.badbird.spear.http.HttpMethod;
import dev.badbird.spear.http.SpearHandler;
import io.javalin.http.Context;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class TestPostController implements SpearHandler {
    @Route(value = "/postme", method = HttpMethod.POST)
    public String postMe(Context ctx, @RequestBody TestObject obj) {
        System.out.println("Received POST request!");
        System.out.println("String: " + obj.aString);
        System.out.println("Int: " + obj.anInt);
        return obj.toString();
    }

    public static class TestObject {
        @NotBlank
        public String aString;
        @Min(10)
        public int anInt;

        @Override
        public String toString() {
            return "TestObject{" +
                    "aString='" + aString + '\'' +
                    ", anInt=" + anInt +
                    '}';
        }
    }
}
