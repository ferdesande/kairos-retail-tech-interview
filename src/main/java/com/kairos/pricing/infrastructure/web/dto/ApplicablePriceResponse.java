package com.kairos.pricing.infrastructure.web.dto;

import com.kairos.pricing.domain.model.Price;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ApplicablePriceResponse(
        Long productId,
        Long brandId,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startDate,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime endDate,
        Integer priceList,
        BigDecimal price,
        String currency
) {
    // Hint: No mapper was created because Price domain model and PriceResponse DTO
    // have a one-to-one correspondence. Consider adding a mapper if complex transformations are needed.
    public static ApplicablePriceResponse from(Price price) {
        return new ApplicablePriceResponse(
                price.productId(),
                price.brandId(),
                price.startDate(),
                price.endDate(),
                price.priceList(),
                price.amount(),
                price.currency()
        );
    }
}