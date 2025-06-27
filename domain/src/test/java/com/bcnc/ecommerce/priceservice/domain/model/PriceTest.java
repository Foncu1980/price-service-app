package com.bcnc.ecommerce.priceservice.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class PriceTest {

    private Price createValidPrice(
            final BigDecimal price,
            final LocalDateTime start,
            final LocalDateTime end
    ) {
        return Price.builder()
                .brandId(1L)
                .startDate(start)
                .endDate(end)
                .priceList(2)
                .productId(35455L)
                .priority(1)
                .price(price)
                .curr("EUR")
                .build();
    }

    private Price.Builder baseBuilder() {
        return Price.builder()
                .brandId(1L)
                .priceList(2)
                .productId(35455L)
                .priority(1)
                .curr("EUR");
    }

    @Test
    @DisplayName("Constructor y getters funcionan correctamente")
    void testConstructorAndGetters() {
        LocalDateTime start = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 6, 14, 18, 30);
        Price price = createValidPrice(new BigDecimal("25.45"), start, end);

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
    void testEqualsAndHashCode() {
        LocalDateTime start = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 6, 14, 18, 30);

        Price price1 = createValidPrice(new BigDecimal("25.45"), start, end);
        Price price2 = createValidPrice(new BigDecimal("25.45"), start, end);

        assertEquals(price1, price2);
        assertEquals(price1.hashCode(), price2.hashCode());
    }

    @Test
    @DisplayName("Equals devuelve false si cambia el precio")
    void testEqualsReturnsFalseForDifferentPrice() {
        LocalDateTime start = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 6, 14, 18, 30);

        Price price1 = createValidPrice(new BigDecimal("25.45"), start, end);
        Price price2 = createValidPrice(new BigDecimal("30.00"), start, end);

        assertNotEquals(price1, price2);
    }

    @Test
    @DisplayName("Equals devuelve false si se compara con null")
    void testEqualsWithNull() {
        LocalDateTime start = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime end = LocalDateTime.of(2020, 6, 14, 18, 30);

        Price price = createValidPrice(new BigDecimal("25.45"), start, end);

        assertNotEquals(null, price);
    }

    @Test
    @DisplayName("toString genera una representación legible con todos los campos clave")
    void testToString() {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 15, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 18, 30);
        Price price = createValidPrice(new BigDecimal("25.45"), startDate, endDate);

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

    @Test
    @DisplayName("Lanza excepción si el precio es negativo")
    void shouldThrowExceptionForNegativePrice() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        assertThrows(IllegalArgumentException.class, () ->
                createValidPrice(new BigDecimal("-1.00"), start, end)
        );
    }

    @Test
    @DisplayName("Lanza excepción si fecha de fin es anterior a la de inicio")
    void shouldThrowExceptionForInvalidDateRange() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.minusHours(1);
        assertThrows(IllegalArgumentException.class, () ->
                createValidPrice(new BigDecimal("10.00"), start, end)
        );
    }

    @Test
    @DisplayName("Lanza excepción si el código de moneda está en blanco")
    void shouldThrowExceptionForEmptyCurrency() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> baseBuilder()
                        .startDate(start)
                        .endDate(end)
                        .price(new BigDecimal("10.00"))
                        .curr(" ")
                        .build()
        );

        assertTrue(exception.getMessage().contains("curr vacío"));
    }

    @Test
    @DisplayName("Lanza excepción si startDate es nulo")
    void shouldThrowExceptionWhenStartDateIsNull() {
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        assertThrows(NullPointerException.class, () ->
                baseBuilder()
                        .startDate(null)
                        .endDate(end)
                        .price(new BigDecimal("10.00"))
                        .build()
        );
    }

    @Test
    @DisplayName("Lanza excepción si brandId es negativo")
    void shouldThrowExceptionForNegativeBrandId() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> baseBuilder()
                        .brandId(-1L)
                        .startDate(start)
                        .endDate(end)
                        .price(new BigDecimal("10.00"))
                        .build()
        );

        assertTrue(exception.getMessage().contains("brandId negativo"));
    }
}
