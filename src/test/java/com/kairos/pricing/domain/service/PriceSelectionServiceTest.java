package com.kairos.pricing.domain.service;

import com.kairos.pricing.domain.exception.UnexpectedApplicationException;
import com.kairos.pricing.domain.model.Price;
import com.kairos.pricing.domain.mother.PriceMother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PriceSelectionServiceTest {
    private final PriceSelectionService service = new PriceSelectionService();

    @ParameterizedTest
    @CsvSource({
            "0:1:2, 2",
            "5:1, 5",
            "0:3:1:2, 3",
            "1, 1"
    })
    @DisplayName("Should select price with highest priority")
    void shouldSelectPriceWithHighestPriority(String priorityIds, int expectedPriority) {
        // Given
        List<Price> prices = createPricesFromString(priorityIds);

        // When
        Price result = service.selectBestPrice(prices);

        // Then
        assertThat(result.priority(), equalTo(expectedPriority));
    }

    @Test
    @DisplayName("Should throw exception when price list is empty")
    void shouldThrowExceptionWhenPriceListIsEmpty() {
        // Given
        List<Price> emptyPrices = Collections.emptyList();

        // When
        UnexpectedApplicationException exception = assertThrows(UnexpectedApplicationException.class, () ->
                service.selectBestPrice(emptyPrices));

        // Then
        assertThat(exception.getMessage(), equalTo("At least one price was expected for selection"));
    }

    private List<Price> createPricesFromString(String priorityIds) {
        return Arrays.stream(priorityIds.split(":"))
                .map(Integer::parseInt)
                .map(PriceMother::withPriority)
                .toList();
    }
}
