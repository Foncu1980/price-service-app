package com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PriceEntityTest {

    @Test
    @DisplayName("Constructor completo inicializa correctamente los campos")
    void testConstructorCompleto() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);

        PriceEntity entity = new PriceEntity(
                1L, start, end, 2, 35455L, 1, new BigDecimal("20.50"), "EUR"
        );

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

        PriceEntity entity1 = new PriceEntity(1L, start, end, 2, 35455L, 1, new BigDecimal("10.00"), "EUR");
        PriceEntity entity2 = new PriceEntity(1L, start, end, 2, 35455L, 1, new BigDecimal("10.00"), "EUR");

        // Aunque no tengan el mismo ID, equals usa todos los campos
        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    @DisplayName("toString no devuelve null y contiene campos clave")
    void testToString() {
        PriceEntity entity = new PriceEntity();
        entity.setBrandId(1L);
        entity.setCurr("EUR");

        String str = entity.toString();
        assertNotNull(str);
        assertTrue(str.contains("brandId=1"));
        assertTrue(str.contains("curr='EUR'"));
    }
}
