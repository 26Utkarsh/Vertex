package com.vertex.common;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<ApiErrorResponse> handleResponseStatus(ResponseStatusException exception) {
        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
        return build(status, exception.getReason() == null ? status.getReasonPhrase() : exception.getReason(), List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorResponse> handleMethodArgument(MethodArgumentNotValidException exception) {
        List<String> details = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return build(HttpStatus.BAD_REQUEST, "Request validation failed", details);
    }

    @ExceptionHandler({ConstraintViolationException.class, HandlerMethodValidationException.class, MethodArgumentTypeMismatchException.class})
    ResponseEntity<ApiErrorResponse> handleValidation(Exception exception) {
        return build(HttpStatus.BAD_REQUEST, "Request validation failed", List.of(exception.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException exception) {
        return build(HttpStatus.FORBIDDEN, "Access denied", List.of());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiErrorResponse> handleUnexpected(Exception exception) {
        LOGGER.error("Unexpected API error", exception);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", List.of());
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message, List<String> details) {
        return ResponseEntity.status(status).body(new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                details
        ));
    }
}
