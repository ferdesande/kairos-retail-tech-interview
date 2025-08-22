package com.kairos.pricing.infrastructure.persistence.mapper;

import com.kairos.pricing.domain.model.Price;
import com.kairos.pricing.infrastructure.persistence.entity.PriceEntity;
import org.springframework.stereotype.Component;

@Component
public class PriceEntityMapper {
    // Hint: Mapper decouples Entities from Domain model
    // allowing independent evolution of persistence and domain layers.
    // Even though the correspondence is one-to-one, the mapper reinforces the decoupling

    public Price toDomain(PriceEntity entity) {
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
}
