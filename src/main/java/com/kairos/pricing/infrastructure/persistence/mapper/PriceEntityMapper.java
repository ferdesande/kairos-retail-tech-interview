package com.kairos.pricing.infrastructure.persistence.mapper;

import com.kairos.pricing.domain.model.Price;
import com.kairos.pricing.infrastructure.persistence.entity.PriceEntity;
import org.springframework.stereotype.Component;

@Component
public class PriceEntityMapper {

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
