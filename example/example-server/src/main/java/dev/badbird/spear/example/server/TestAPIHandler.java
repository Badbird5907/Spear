package dev.badbird.spear.example.server;

import dev.badbird.spear.http.APIHandler;
import dev.badbird.spear.example.common.TestAPIRequest;
import io.javalin.http.Context;

public class TestAPIHandler implements APIHandler<TestAPIRequest.ResponseType, TestAPIRequest> {
    @Override
    public TestAPIRequest.ResponseType handle(TestAPIRequest request, Context ctx) {
        System.out.println("Received request: " + request.str() + " " + request.num());
        return new TestAPIRequest.ResponseType("Hello world!");
    }
}
