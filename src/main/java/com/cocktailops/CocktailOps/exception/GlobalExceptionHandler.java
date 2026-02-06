package com.cocktailops.CocktailOps.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.util.List;



@RestControllerAdvice
public class GlobalExceptionHandler {


    public record ApiError(
            Instant timestamp,
            int status,
            String error,
            String message,
            String path,
            List<FieldViolation> violations
    ){}

    public record  FieldViolation(String field, String message){}

    private ApiError build(HttpStatus status, String message, String path, List<FieldViolation> violations) {
        return new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                violations == null ? List.of() : violations
        );
    }

    @ExceptionHandler({
            ResourceNotFoundException.class,
            DuplicateResourceException.class,
            BadRequestException.class,
            InvalidCredentialsException.class,
            ForbiddenException.class,
            BusinessRuleException.class
    })
    public ResponseEntity<ApiError> handleCustom(RuntimeException ex, HttpServletRequest request) {
        HttpStatus status;
        if (ex instanceof ResourceNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof DuplicateResourceException) {
            status = HttpStatus.CONFLICT;
        } else if (ex instanceof BadRequestException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof InvalidCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof ForbiddenException) {
            status = HttpStatus.FORBIDDEN;
        } else if (ex instanceof BusinessRuleException) {
            status = HttpStatus.UNPROCESSABLE_CONTENT; // o BAD_REQUEST
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status)
                .body(build(status, ex.getMessage(), request.getRequestURI(), List.of()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<FieldViolation> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new FieldViolation(err.getField(), err.getDefaultMessage()))
                .toList();

        return ResponseEntity.status(status)
                .body(build(status, "Validation failed", req.getRequestURI(), violations));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status)
                .body(build(status, "Conflicto de datos (posible duplicado).", req.getRequestURI(), List.of()));
    }
}
