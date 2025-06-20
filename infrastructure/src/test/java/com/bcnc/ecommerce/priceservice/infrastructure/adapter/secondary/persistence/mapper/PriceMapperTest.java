package com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.mapper;

import com.bcnc.ecommerce.priceservice.domain.model.Price;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.entity.PriceEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PriceMapperTest
{
    private final PriceMapper mapper = new PriceMapper();

    @Test
    @DisplayName("Mapeo correcto de PriceEntity a Price")
    void testToDomain()
    {
        PriceEntity entity = new PriceEntity();
        entity.setBrandId(1L);
        entity.setStartDate(LocalDateTime.of(2020, 6, 14, 15, 0));
        entity.setEndDate(LocalDateTime.of(2020, 6, 14, 18, 30));
        entity.setPriceList(2);
        entity.setProductId(35455L);
        entity.setPriority(1);
        entity.setPrice(new BigDecimal("25.45"));
        entity.setCurr("EUR");

        Price domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getBrandId(), domain.getBrandId());
        assertEquals(entity.getStartDate(), domain.getStartDate());
        assertEquals(entity.getEndDate(), domain.getEndDate());
        assertEquals(entity.getPriceList(), domain.getPriceList());
        assertEquals(entity.getProductId(), domain.getProductId());
        assertEquals(entity.getPriority(), domain.getPriority());
        assertEquals(entity.getPrice(), domain.getPrice());
        assertEquals(entity.getCurr(), domain.getCurr());
    }

    @Test
    @DisplayName("Mapeo correcto de Price a PriceEntity")
    void testToEntity()
    {
        Price price = new Price(1L,
                LocalDateTime.of(2020, 6, 15, 0, 0),
                LocalDateTime.of(2020, 6, 15, 11, 0),
                3, 35455L, 1, new BigDecimal("30.50"), "EUR");

        PriceEntity entity = mapper.toEntity(price);

        assertNotNull(entity);
        assertEquals(price.getBrandId(), entity.getBrandId());
        assertEquals(price.getStartDate(), entity.getStartDate());
        assertEquals(price.getEndDate(), entity.getEndDate());
        assertEquals(price.getPriceList(), entity.getPriceList());
        assertEquals(price.getProductId(), entity.getProductId());
        assertEquals(price.getPriority(), entity.getPriority());
        assertEquals(price.getPrice(), entity.getPrice());
        assertEquals(price.getCurr(), entity.getCurr());
    }
}
