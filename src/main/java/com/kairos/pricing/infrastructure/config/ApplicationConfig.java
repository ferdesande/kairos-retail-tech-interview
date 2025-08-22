package com.kairos.pricing.infrastructure.config;

import com.kairos.pricing.application.usecase.GetPriceUseCase;
import com.kairos.pricing.domain.port.PriceRepository;
import com.kairos.pricing.domain.service.PriceSelectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public PriceSelectionService priceSelectionService() {
        return new PriceSelectionService();
    }

    @Bean
    public GetPriceUseCase getPriceUseCase(
            PriceRepository priceRepository,
            PriceSelectionService priceSelectionService) {
        return new GetPriceUseCase(priceRepository, priceSelectionService);
    }
}