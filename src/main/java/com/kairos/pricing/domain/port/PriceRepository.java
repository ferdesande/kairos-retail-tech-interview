package com.kairos.pricing.domain.port;

import com.kairos.pricing.domain.model.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository {
    List<Price> findValidPricesForDate(LocalDateTime applicationDate, Long productId, Long brandId);
}
