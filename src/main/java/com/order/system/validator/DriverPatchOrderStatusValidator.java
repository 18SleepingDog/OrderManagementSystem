package com.order.system.validator;

import com.order.system.annotation.DriverPatchOrderStatusPattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class DriverPatchOrderStatusValidator implements ConstraintValidator<DriverPatchOrderStatusPattern, Enum<?>> {
    private Pattern pattern;

    @Override
    public void initialize(DriverPatchOrderStatusPattern annotation) {
        try {
            pattern = Pattern.compile(annotation.regexp());
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Given regex is invalid", e);
        }
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Matcher m = pattern.matcher(value.name());
        return m.matches();
    }
}
