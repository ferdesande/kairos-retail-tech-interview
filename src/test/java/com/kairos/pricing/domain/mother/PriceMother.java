package com.kairos.pricing.domain.mother;

import com.kairos.pricing.domain.model.Price;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceMother {

    public static Price defaultPrice() {
        return withPriority(0);
    }

    public static Price withPriority(int priority) {
        return new Price(
                101L,
                1L,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59),
                priority,
                new BigDecimal("35.50"),
                "EUR"
        );
    }
}
