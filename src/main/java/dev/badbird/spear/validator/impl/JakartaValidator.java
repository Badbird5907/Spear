package dev.badbird.spear.validator.impl;

import dev.badbird.spear.validator.SpearValidator;
import dev.badbird.spear.validator.ValidatorException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JakartaValidator implements SpearValidator { // TODO: better validator implementation
    public static final JakartaValidator INSTANCE = new JakartaValidator();
    private boolean enabled = false;
    private ValidatorFactory factory;
    private JakartaValidator() {
        try {
            Class.forName("jakarta.validation.ValidatorFactory");
            enabled = true;
        } catch (ClassNotFoundException e) {
            enabled = false;
        }

        if (enabled) {
            try {
                factory = Validation.byDefaultProvider()
                        .configure()
                        .messageInterpolator(new ParameterMessageInterpolator())
                        .buildValidatorFactory();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to build Jakarta Validation factory", e);
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void validate(Object in) {
        if (enabled) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Object>> violations = validator.validate(in);
            if (!violations.isEmpty()) {
                List<String> messages = new ArrayList<>();
                for (ConstraintViolation<Object> violation : violations) {
                    String s = violation.getPropertyPath().toString() + " " + violation.getMessage();
                    messages.add(s);
                }
                throw new ValidatorException(messages);
            }
        }
    }
}
