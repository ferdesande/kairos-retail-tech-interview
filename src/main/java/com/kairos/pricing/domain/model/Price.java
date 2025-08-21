package com.kairos.pricing.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Price(
        Long productId,
        Long brandId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer priority,
        BigDecimal amount,
        String currency
) {
    // No validation required in spec
}
