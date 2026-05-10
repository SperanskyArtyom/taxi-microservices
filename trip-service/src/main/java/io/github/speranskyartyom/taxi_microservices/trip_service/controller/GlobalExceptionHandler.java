package io.github.speranskyartyom.taxi_microservices.trip_service.controller;

import feign.FeignException;
import io.github.speranskyartyom.taxi_microservices.common.dto.ErrorResponse;
import io.github.speranskyartyom.taxi_microservices.common.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {
        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;

        String responseBody = ex.contentUTF8();
        String cleanMessage = responseBody.contains("\"message\":\"")
                ? responseBody.split("\"message\":\"")[1].split("\"")[0]
                : responseBody;

        ErrorResponse error = ErrorResponse.builder()
                .message("User service error: " + cleanMessage)
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .build();

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFound(ResourceNotFoundException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ErrorResponse.builder()
                .message("Validation error")
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(errors)
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAll(RuntimeException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }
}
