package com.kairos.pricing.infrastructure.persistence.repository;

import com.kairos.pricing.infrastructure.persistence.entity.PriceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface PriceJpaRepository extends CrudRepository<PriceEntity, Long> {
    // Hint: defined as @Component because only repository adapters must be annotated as @Repository

    @Query("SELECT p FROM PriceEntity p WHERE " +
            "p.productId = :productId AND " +
            "p.brandId = :brandId AND " +
            ":date BETWEEN p.startDate AND p.endDate")
    List<PriceEntity> findValidPricesForDate(
            @Param("date") LocalDateTime applicationDate,
            @Param("productId") Long productId,
            @Param("brandId") Long brandId);
}
