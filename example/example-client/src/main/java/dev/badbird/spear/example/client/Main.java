package dev.badbird.spear.example.client;

import com.google.gson.Gson;
import dev.badbird.spear.SpearCommons;
import dev.badbird.spear.example.common.TestAPIRequest;

public class Main {
    public static void main(String[] args) {
        new SpearCommons(new Gson(), "http://localhost:8089", builder -> builder);
        new TestAPIRequest("Hi", 1337).send().thenAccept(System.out::println);
        System.out.println("Done");
    }
}