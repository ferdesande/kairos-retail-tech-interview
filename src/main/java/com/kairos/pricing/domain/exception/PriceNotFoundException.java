package com.kairos.pricing.domain.exception;

public class PriceNotFoundException extends DomainException {
    public PriceNotFoundException(String message) {
        super(message);
    }
}
