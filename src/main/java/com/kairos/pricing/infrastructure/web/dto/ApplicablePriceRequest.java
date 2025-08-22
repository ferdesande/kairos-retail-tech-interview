package com.kairos.pricing.infrastructure.web.dto;

import com.kairos.pricing.domain.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record ApplicablePriceRequest(
        LocalDateTime applicationDate,
        Long productId,
        Long brandId) {

    public void validate() {
        List<String> errors = new ArrayList<>();

        if (applicationDate == null) errors.add("Application date is required");
        if (productId == null || productId <= 0) errors.add("Product ID must be positive");
        if (brandId == null || brandId <= 0) errors.add("Brand ID must be positive");

        if (!errors.isEmpty()) {
            throw new ValidationException("PriceRequest is invalid", errors);
        }
    }
}