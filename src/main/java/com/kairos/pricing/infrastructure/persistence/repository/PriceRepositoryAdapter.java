package com.kairos.pricing.infrastructure.persistence.repository;

import com.kairos.pricing.domain.model.Price;
import com.kairos.pricing.domain.port.PriceRepository;
import com.kairos.pricing.infrastructure.persistence.mapper.PriceEntityMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PriceRepositoryAdapter implements PriceRepository {
    
    private final PriceJpaRepository jpaRepository;
    private final PriceEntityMapper mapper;
    
    public PriceRepositoryAdapter(PriceJpaRepository jpaRepository, PriceEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public List<Price> findValidPricesForDate(LocalDateTime applicationDate, Long productId, Long brandId) {
        return jpaRepository.findValidPricesForDate(applicationDate, productId, brandId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
