package com.kairos.pricing.domain.service;

import com.kairos.pricing.domain.model.Price;

import java.util.List;

public interface PriceSelectionService {
    Price selectBestPrice(List<Price> prices);
}
