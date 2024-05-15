package dev.badbird.spear.inject;

import com.google.inject.Injector;
import dev.badbird.spear.Spear;
import dev.badbird.spear.http.SpearHandler;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;

@RequiredArgsConstructor
public class SpearGuiceAdapter {
    private final Spear spear;

    public Spear registerPackage(String packageName, Injector injector) {
        for (Class<? extends SpearHandler> aClass : new Reflections(packageName).getSubTypesOf(SpearHandler.class)) {
            Object instance = injector.getInstance(aClass);
            System.out.println("Registering " + instance.getClass().getName());
            spear.register((SpearHandler) instance);
        }
        return spear;
    }
}
