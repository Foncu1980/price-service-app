package com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bcnc.ecommerce.priceservice.domain.model.Price;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.entity.PriceEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @DisplayName("Mapeo de PriceEntity con campos nulos a Price debe lanzar NullPointerException")
    void testToDomainWithNullFields() {
        PriceEntity entityWithNulls = new PriceEntity();
        // Solo seteamos algunos campos para simular nulos en otros
        entityWithNulls.setBrandId(1L);
        entityWithNulls.setProductId(123L);
        // startDate, endDate, priceList, priority, price, curr serán nulos

        // Esperamos que el constructor de Price lance IllegalArgumentException
        // debido a los campos nulos que no son permitidos por el dominio.
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            mapper.toDomain(entityWithNulls);
        });

        // Opcional: verificar el mensaje de la excepción si es relevante
        assertTrue(thrown.getMessage().contains("startDate no puede ser nulo"));
    }

    @Test
    @DisplayName("Mapeo correcto de Price a PriceEntity")
    void testToEntity()
    {
        Price price = Price.builder()
                .brandId(1L)
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
                .priceList(1)
                .productId(35455L)
                .priority(0)
                .price(new BigDecimal("35.50"))
                .curr("EUR")
                .build();

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

    @Test
    @DisplayName("Mapeo de Price a PriceEntity con BigDecimal cero")
    void testToEntityWithZeroPrice() {
        Price price = Price.builder()
                .brandId(1L)
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
                .priceList(1)
                .productId(35455L)
                .priority(0)
                .price(BigDecimal.ZERO)
                .curr("EUR")
                .build();

        PriceEntity entity = mapper.toEntity(price);

        assertNotNull(entity);
        assertEquals(BigDecimal.ZERO, entity.getPrice());
    }
}
