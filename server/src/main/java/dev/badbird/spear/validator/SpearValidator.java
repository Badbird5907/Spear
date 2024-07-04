package dev.badbird.spear.validator;

public interface SpearValidator {
    boolean isEnabled();
    void validate(Object in);
}
