package com.order.system.validator;
import com.order.system.annotation.LatitudeLongitudeListPattern;
import jakarta.validation.ConstraintValidator;

import java.util.List;

public class LatitudeLongitudeListValidator implements ConstraintValidator<LatitudeLongitudeListPattern, List<String>> {

    @Override
    public boolean isValid(List<String> value, jakarta.validation.ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null values are handled by @NotNull or @Size annotations
        }
        if (value.size() != 2) {
            return false;
        }


        try {
            double latitude = Double.parseDouble(value.get(0).trim());
            double longitude = Double.parseDouble(value.get(1).trim());
            return (latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
