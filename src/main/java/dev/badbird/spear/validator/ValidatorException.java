package dev.badbird.spear.validator;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidatorException extends IllegalArgumentException {
    private List<String> messages;
    public ValidatorException(List<String> messages) {
        super(String.join("\n", messages));
        this.messages = messages;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
