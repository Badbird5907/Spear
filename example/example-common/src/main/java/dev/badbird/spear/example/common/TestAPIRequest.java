package dev.badbird.spear.example.common;

import dev.badbird.spear.rest.APIRequest;
import dev.badbird.spear.rest.Route;

@Route(value = "/test")
public record TestAPIRequest(String str, int num) implements APIRequest<TestAPIRequest.ResponseType> {
    public record ResponseType(String str) {
        @Override
        public String toString() {
            return str;
        }
    }
}
