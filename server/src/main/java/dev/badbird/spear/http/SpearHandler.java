package dev.badbird.spear.http;

import dev.badbird.spear.rest.Route;

public interface SpearHandler {
    default Route getRoute() {
        return getClass().getAnnotation(Route.class);
    }
}
