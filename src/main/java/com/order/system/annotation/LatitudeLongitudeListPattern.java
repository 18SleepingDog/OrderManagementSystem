package com.order.system.annotation;

import com.order.system.validator.LatitudeLongitudeListValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = LatitudeLongitudeListValidator.class)
public @interface LatitudeLongitudeListPattern {
    String message() default "Invalid coordinate";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}