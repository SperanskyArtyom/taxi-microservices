package io.github.speranskyartyom.taxi_microservices.trip_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LocationValidator implements ConstraintValidator<IsLocation, String> {
    private static final String REGEX = "^-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return value.matches(REGEX);
    }
}
