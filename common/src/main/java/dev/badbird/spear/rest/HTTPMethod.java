package dev.badbird.spear.rest;

public enum HTTPMethod {
    GET(false),
    POST(true),
    PUT(true),
    DELETE(false),
    PATCH(true);

    boolean hasBody;
    HTTPMethod(boolean hasBody) {
        this.hasBody = hasBody;
    }

    public boolean isHasBody() {
        return hasBody;
    }
}