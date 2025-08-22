package com.kairos.pricing.infrastructure.web.exception;

import com.kairos.pricing.domain.exception.DomainException;
import com.kairos.pricing.domain.exception.PriceNotFoundException;
import com.kairos.pricing.domain.exception.UnexpectedApplicationException;
import com.kairos.pricing.domain.exception.ValidationException;
import com.kairos.pricing.infrastructure.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Hint: Errors must be logged to aid debugging.
    // Consider adding a proper logging library if the project evolves.

    private static final String INTERNAL_SERVER_ERROR_TYPE = "Internal Server Error";
    private static final String NOT_FOUND_TYPE = "Not Found";
    private static final String UNEXPECTED_APPLICATION_ERROR_TYPE = "Unexpected Application Error";
    private static final String VALIDATION_ERROR_TYPE = "Validation Error";

    private static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred";

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        return switch (ex) {
            case PriceNotFoundException pnf -> handleError(NOT_FOUND_TYPE, pnf, HttpStatus.NOT_FOUND);

            case ValidationException ve -> handleError(VALIDATION_ERROR_TYPE,
                    ex.getMessage() + ((ve.getErrors().size() > 1) ? ":\n" : ": ") + ve.formatErrors(),
                    HttpStatus.BAD_REQUEST);

            case UnexpectedApplicationException uae ->
                    handleError(UNEXPECTED_APPLICATION_ERROR_TYPE, uae, HttpStatus.INTERNAL_SERVER_ERROR);

            default ->
                    handleError(INTERNAL_SERVER_ERROR_TYPE, UNEXPECTED_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentNotValidException ex) {
        String fieldName = ex.getFieldError() != null ? ex.getFieldError().getField() : "unknown";
        return handleError(VALIDATION_ERROR_TYPE,
                String.format("Invalid format for parameter '%s'", fieldName),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return handleError(INTERNAL_SERVER_ERROR_TYPE, UNEXPECTED_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> handleError(String type, String message, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(type, message, status.value()));
    }

    private ResponseEntity<ErrorResponse> handleError(String type, Throwable ex, HttpStatus status) {
        return handleError(type, ex.getMessage(), status);
    }
}