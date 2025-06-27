package com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PriceEntityTest {

    @Test
    @DisplayName("Builder inicializa correctamente los campos")
    void testBuilder() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);

        PriceEntity entity = buildPriceEntity(start, end, new BigDecimal("20.50"));

        assertEquals(1L, entity.getBrandId());
        assertEquals(start, entity.getStartDate());
        assertEquals(end, entity.getEndDate());
        assertEquals(2, entity.getPriceList());
        assertEquals(35455L, entity.getProductId());
        assertEquals(1, entity.getPriority());
        assertEquals(new BigDecimal("20.50"), entity.getPrice());
        assertEquals("EUR", entity.getCurr());
    }

    @Test
    @DisplayName("equals y hashCode funcionan correctamente")
    void testEqualsAndHashCode() {
        LocalDateTime start = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime end = start.plusHours(1);
        BigDecimal price = new BigDecimal("10.00");

        PriceEntity entity1 = buildPriceEntity(start, end, price);
        PriceEntity entity2 = buildPriceEntity(start, end, price);

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    @DisplayName("toString no devuelve null y contiene campos clave")
    void testToString() {
        PriceEntity entity = new PriceEntity.Builder()
                .brandId(1L)
                .curr("EUR")
                .build();

        String str = entity.toString();
        assertNotNull(str);
        assertTrue(str.contains("brandId=1"));
        assertTrue(str.contains("curr='EUR'"));
    }

    /**
     * Crea una instancia de PriceEntity con valores comunes
     * para simplificar los tests.
     *
     * @param startDate fecha de inicio
     * @param endDate   fecha de fin
     * @param price     valor monetario
     * @return instancia de PriceEntity
     */
    private PriceEntity buildPriceEntity(LocalDateTime startDate,
                                         LocalDateTime endDate,
                                         BigDecimal price) {
        return PriceEntity.builder()
                .brandId(1L)
                .startDate(startDate)
                .endDate(endDate)
                .priceList(2)
                .productId(35455L)
                .priority(1)
                .price(price)
                .curr("EUR")
                .build();
    }
}
