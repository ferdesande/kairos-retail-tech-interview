package com.kairos.pricing.infrastructure.persistence.repository;

import com.kairos.pricing.domain.model.Price;
import com.kairos.pricing.infrastructure.persistence.entity.PriceEntity;
import com.kairos.pricing.infrastructure.persistence.mapper.PriceEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@Import(value = {PriceEntityMapper.class})
@Sql(scripts = "/schema.sql")
class PriceRepositoryAdapterTest {
    // Hint: the annotation @Sql only loads the schema. It executes schema.sql and data.sql if omitted.
    // If a migration system is added further, maybe this must be adapted.
    // more info at https://docs.spring.io/spring-framework/reference/testing/testcontext-framework/executing-sql.html
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private PriceEntityMapper mapper;

    @Autowired
    private PriceJpaRepository repository;

    private PriceRepositoryAdapter adapter;

    // Test data constants
    private static final Long BRAND_ID = 2L;  // Different from data.sql to avoid conflicts
    private static final Long PRODUCT_ID = 12345L;
    private static final LocalDateTime BASE_DATE = LocalDateTime.of(2024, 1, 15, 10, 0);

    // Semi-overlapping prices for comprehensive testing
    private static Price earlyPrice;  // 2024-01-15 08:00 - 2024-01-15 14:00 (priority 0)
    private static Price latePrice;   // 2024-01-15 12:00 - 2024-01-15 18:00 (priority 1)

    private static Price toDomain(PriceEntity entity) {
        return new Price(
                entity.getProductId(),
                entity.getBrandId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPriceList(),
                entity.getPriority(),
                entity.getPrice(),
                entity.getCurrency()
        );
    }

    @BeforeEach
    void setUp() {
        adapter = new PriceRepositoryAdapter(repository, mapper);

        // Create semi-overlapping test prices
        PriceEntity earlyPriceEntity = new PriceEntity(
                BRAND_ID,
                BASE_DATE.minusHours(2),     // 08:00
                BASE_DATE.plusHours(4),      // 14:00
                1,
                PRODUCT_ID,
                0,
                new BigDecimal("25.00"),
                "EUR"
        );

        PriceEntity latePriceEntity = new PriceEntity(
                BRAND_ID,
                BASE_DATE.plusHours(2),      // 12:00
                BASE_DATE.plusHours(8),      // 18:00
                2,
                PRODUCT_ID,
                1,
                new BigDecimal("30.00"),
                "EUR"
        );

        entityManager.persistAndFlush(earlyPriceEntity);
        entityManager.persistAndFlush(latePriceEntity);

        earlyPrice = toDomain(earlyPriceEntity);
        latePrice = toDomain(latePriceEntity);
    }

    @Test
    @DisplayName("return early price only when date is before overlap period")
    void returnEarlyPriceOnlyWhenDateBeforeOverlap() {
        // Given: 09:00 - only early price applies
        LocalDateTime queryDate = BASE_DATE.minusHours(1);

        // When
        List<Price> result = adapter.findValidPricesForDate(queryDate, PRODUCT_ID, BRAND_ID);

        // Then
        assertThat(result, contains(earlyPrice));
    }

    @Test
    @DisplayName("return late price only when date is after overlap period")
    void returnLatePriceOnlyWhenDateAfterOverlap() {
        // Given: 16:00 - only late price applies
        LocalDateTime queryDate = BASE_DATE.plusHours(6);

        // When
        List<Price> result = adapter.findValidPricesForDate(queryDate, PRODUCT_ID, BRAND_ID);

        // Then
        assertThat(result, contains(latePrice));
    }

    @Test
    @DisplayName("return both prices when date is in overlap period")
    void returnBothPricesWhenDateInOverlapPeriod() {
        // Given: 13:00 - both prices apply (overlap period)
        LocalDateTime queryDate = BASE_DATE.plusHours(3);

        // When
        List<Price> result = adapter.findValidPricesForDate(queryDate, PRODUCT_ID, BRAND_ID);

        // Then
        assertThat(result, containsInAnyOrder(earlyPrice, latePrice));
    }

    @Test
    @DisplayName("return no prices when date is outside all ranges")
    void returnNoPricesWhenDateOutsideAllRanges() {
        // Given: 20:00 - no prices apply
        LocalDateTime queryDate = BASE_DATE.plusHours(10);

        // When
        List<Price> result = adapter.findValidPricesForDate(queryDate, PRODUCT_ID, BRAND_ID);

        // Then
        assertThat(result, emptyIterable());
    }

    @Test
    @DisplayName("return no prices when brand does not exist")
    void returnNoPricesWhenBrandDoesNotExist() {
        // Given
        Long nonExistentBrand = 999L;

        // When
        List<Price> result = adapter.findValidPricesForDate(BASE_DATE, PRODUCT_ID, nonExistentBrand);

        // Then
        assertThat(result, emptyIterable());
    }

    @Test
    @DisplayName("return no prices when product does not exist")
    void returnNoPricesWhenProductDoesNotExist() {
        // Given
        Long nonExistentProduct = 999L;

        // When
        List<Price> result = adapter.findValidPricesForDate(BASE_DATE, nonExistentProduct, BRAND_ID);

        // Then
        assertThat(result, emptyIterable());
    }
}