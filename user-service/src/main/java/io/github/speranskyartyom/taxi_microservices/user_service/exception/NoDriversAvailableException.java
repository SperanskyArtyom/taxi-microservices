package io.github.speranskyartyom.taxi_microservices.user_service.exception;

public class NoDriversAvailableException extends RuntimeException {
    public NoDriversAvailableException(String message) {
        super(message);
    }
}
