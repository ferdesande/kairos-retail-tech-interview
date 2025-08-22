package com.kairos.pricing.application.usecase;

import com.kairos.pricing.domain.exception.PriceNotFoundException;
import com.kairos.pricing.domain.model.Price;
import com.kairos.pricing.domain.port.PriceRepository;
import com.kairos.pricing.domain.service.PriceSelectionService;

import java.time.LocalDateTime;
import java.util.List;

public class GetPriceUseCase {
    private final PriceRepository repository;
    private final PriceSelectionService selectionService;

    public GetPriceUseCase(PriceRepository priceRepository, PriceSelectionService selectionService) {
        this.repository = priceRepository;
        this.selectionService = selectionService;
    }

    public Price getApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        List<Price> prices = repository.findValidPricesForDate(applicationDate, productId, brandId);

        if (prices.isEmpty()) {
            throw new PriceNotFoundException("No valid price found");
        }

        return selectionService.selectBestPrice(prices);
    }
}
