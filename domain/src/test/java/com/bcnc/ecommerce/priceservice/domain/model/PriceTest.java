package com.bcnc.ecommerce.priceservice.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class PriceTest
{
    @Test
    @DisplayName("Constructor y getters funcionan correctamente")
    void testConstructorAndGetters()
    {
        LocalDateTime start = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 6, 14, 18, 30);
        Price price = new Price(1L, start, end, 2, 35455L, 1, new BigDecimal("25.45"), "EUR");

        assertEquals(1L, price.getBrandId());
        assertEquals(start, price.getStartDate());
        assertEquals(end, price.getEndDate());
        assertEquals(2, price.getPriceList());
        assertEquals(35455L, price.getProductId());
        assertEquals(1, price.getPriority());
        assertEquals(new BigDecimal("25.45"), price.getPrice());
        assertEquals("EUR", price.getCurr());
    }

    @Test
    @DisplayName("Equals y hashCode funcionan correctamente")
    void testEqualsAndHashCode()
    {
        LocalDateTime start = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 6, 14, 18, 30);

        Price price1 = new Price(1L, start, end, 2, 35455L, 1, new BigDecimal("25.45"), "EUR");
        Price price2 = new Price(1L, start, end, 2, 35455L, 1, new BigDecimal("25.45"), "EUR");

        assertEquals(price1, price2);
        assertEquals(price1.hashCode(), price2.hashCode());
    }

    @Test
    @DisplayName("toString genera una representación legible con todos los campos clave")
    void testToString()
    {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 15, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 18, 30);
        Price price = new Price(1L,startDate,endDate,2,35455L,1,new BigDecimal("25.45"),"EUR");

        String output = price.toString();

        assertAll(
                () -> assertNotNull(output),
                () -> assertTrue(output.contains("brandId=1")),
                () -> assertTrue(output.contains("startDate=2020-06-14T15:00")),
                () -> assertTrue(output.contains("endDate=2020-06-14T18:30")),
                () -> assertTrue(output.contains("priceList=2")),
                () -> assertTrue(output.contains("productId=35455")),
                () -> assertTrue(output.contains("priority=1")),
                () -> assertTrue(output.contains("price=25.45")),
                () -> assertTrue(output.contains("curr='EUR'"))
        );
    }
}
