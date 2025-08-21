package com.kairos.pricing.application.usecase;

import com.kairos.pricing.domain.exception.PriceNotFoundException;
import com.kairos.pricing.domain.model.Price;
import com.kairos.pricing.domain.mother.PriceMother;
import com.kairos.pricing.domain.port.PriceRepository;
import com.kairos.pricing.domain.service.PriceSelectionService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPriceUseCaseTest {

    private final Long SAMPLE_PRODUCT_ID = 19L;
    private final Long SAMPLE_BRAND_ID = 31L;
    private final LocalDateTime SAMPLE_APPLICATION_DATE = LocalDateTime.of(2020, 3, 7, 12, 45);

    @Mock
    private PriceRepository repository;

    @Mock
    private PriceSelectionService selectionService;

    private GetPriceUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetPriceUseCase(repository, selectionService);
    }

    @DisplayName("Best price is returned when data is fetched from repository")
    @Test
    void bestPriceIsReturnedWhenDataIsFetchedFromRepository() {
        // Given
        Price price1 = PriceMother.defaultPrice();
        Price price2 = PriceMother.withPriority(3);
        configureRepositoryResponse(price1, price2);
        configureSelectedPrice(List.of(price1, price2), price1);

        // When
        Price actual = useCase.getPrice(SAMPLE_APPLICATION_DATE, SAMPLE_PRODUCT_ID, SAMPLE_BRAND_ID);

        // Then
        assertThat(actual, Matchers.sameInstance(price1));
    }

    @DisplayName("Throws price not found exception when no data is fetched from repository")
    @Test
    void ThrowsPriceNotFoundExceptionWhenNoDataIsFetchedFromRepository() {
        // Given
        configureRepositoryResponse();

        // When
        PriceNotFoundException exception = assertThrows(PriceNotFoundException.class, () ->
                useCase.getPrice(SAMPLE_APPLICATION_DATE, SAMPLE_PRODUCT_ID, SAMPLE_BRAND_ID)
        );

        // Then
        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), equalTo("No valid price found"));
    }

    private void configureRepositoryResponse(Price... prices) {
        when(repository.findValidPricesForDate(SAMPLE_APPLICATION_DATE, SAMPLE_PRODUCT_ID, SAMPLE_BRAND_ID))
                .thenReturn(List.of(prices));
    }

    private void configureSelectedPrice(List<Price> prices, Price selected) {
        when(selectionService.selectBestPrice(prices)).thenReturn(selected);
    }
}