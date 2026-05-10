package io.github.speranskyartyom.taxi_microservices.trip_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LocationValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsLocation {
    String message() default "Некорректный формат локации. Ожидается 'lat,lon' (напр. 55.0445,82.9174)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}