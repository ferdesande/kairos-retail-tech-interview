package com.kairos.pricing.infrastructure.web.controller;

import com.kairos.pricing.application.usecase.GetPriceUseCase;
import com.kairos.pricing.domain.model.Price;
import com.kairos.pricing.infrastructure.web.dto.ApplicablePriceRequest;
import com.kairos.pricing.infrastructure.web.dto.ApplicablePriceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prices")
public class PriceController {

    // Hint: If multiple price operations are needed in the future (e.g., list all prices),
    // consider using different endpoints like /prices/search or /prices/applicable
    // to avoid ambiguity in query parameters

    private final GetPriceUseCase getPriceUseCase;

    public PriceController(GetPriceUseCase getPriceUseCase) {
        this.getPriceUseCase = getPriceUseCase;
    }

    @GetMapping
    public ResponseEntity<ApplicablePriceResponse> getPrice(@ModelAttribute ApplicablePriceRequest request) {
        request.validate();

        Price price = getPriceUseCase.getPrice(request.applicationDate(), request.productId(), request.brandId());
        ApplicablePriceResponse response = ApplicablePriceResponse.from(price);

        return ResponseEntity.ok(response);
    }
}