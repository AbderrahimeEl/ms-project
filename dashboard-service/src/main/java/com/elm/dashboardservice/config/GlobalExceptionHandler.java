package com.elm.dashboardservice.config;

import io.grpc.StatusRuntimeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<Map<String, String>> handleGrpcException(StatusRuntimeException e) {
        return ResponseEntity.status(503) // Service Unavailable
                .body(Map.of(
                        "error", "Backend service unavailable",
                        "message", e.getStatus().getDescription(),
                        "code", e.getStatus().getCode().name()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception e) {
        return ResponseEntity.internalServerError()
                .body(Map.of(
                        "error", "Internal server error",
                        "message", e.getMessage()
                ));
    }
}