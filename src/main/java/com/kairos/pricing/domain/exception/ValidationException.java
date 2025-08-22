package com.kairos.pricing.domain.exception;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationException extends DomainException {
    private final List<String> errors;

    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String formatErrors() {
        return switch (errors.size()) {
            case 0 -> "Unknown validation error";
            case 1 -> errors.getFirst();
            default -> errors.stream().map(s -> "- " + s).collect(Collectors.joining("\n"));
        };
    }
}
