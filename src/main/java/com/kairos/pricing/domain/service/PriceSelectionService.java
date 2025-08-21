package com.kairos.pricing.domain.service;

import com.kairos.pricing.domain.exception.UnexpectedApplicationException;
import com.kairos.pricing.domain.model.Price;

import java.util.Comparator;
import java.util.List;

public class PriceSelectionService {
    /**
     * Hint:
     * Currently, Implements is used instead of an interface for simplicity
     * Implements standard priority-based selection.
     * Can be extracted to interface if multiple selection strategies are needed.
     */
    public Price selectBestPrice(List<Price> prices) {
        if (prices.isEmpty()) {
            throw new UnexpectedApplicationException("At least one price was expected for selection");
        }

        return prices.stream()
                .max(Comparator.comparing(Price::priority))
                .get();
    }
}
