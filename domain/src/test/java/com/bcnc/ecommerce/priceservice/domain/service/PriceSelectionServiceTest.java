package com.bcnc.ecommerce.priceservice.domain.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bcnc.ecommerce.priceservice.domain.exception.PriceNotFoundException;
import com.bcnc.ecommerce.priceservice.domain.model.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

class PriceSelectionServiceTest {

    private static final long PRODUCT_ID = 35455L;
    private static final long BRAND_ID = 1L;

    private PriceSelectionService service;

    @BeforeEach
    void setUp() {
        service = new PriceSelectionService();
    }

    private Price createPrice(
            final long brandId,
            final LocalDateTime start,
            final LocalDateTime end,
            final int priceList,
            final long productId,
            final int priority,
            final BigDecimal price
    ) {
        return new Price.Builder()
                .brandId(brandId)
                .startDate(start)
                .endDate(end)
                .priceList(priceList)
                .productId(productId)
                .priority(priority)
                .price(price)
                .curr("EUR")
                .build();
    }

    @Test
    @DisplayName("Devuelve el precio cuando solo hay uno aplicable")
    void shouldReturnSingleApplicablePrice() {
        LocalDateTime localTime = LocalDateTime.of(2020, 6, 14, 12, 0);

        Price price = createPrice(BRAND_ID, localTime.minusHours(1), localTime.plusHours(1),
                1, PRODUCT_ID, 0, new BigDecimal("20.00"));

        assertDoesNotThrow(() -> {
            Price result = service.selectApplicablePrice(
                    List.of(price), localTime, PRODUCT_ID, BRAND_ID);

            assertEquals(price, result);
        });
    }

    @Test
    @DisplayName("Devuelve el precio con mayor prioridad entre múltiples aplicables")
    void shouldReturnHighestPriorityPrice() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);

        Price lowPriority = createPrice(BRAND_ID, date.minusHours(2), date.plusHours(2),
                1, PRODUCT_ID, 0, new BigDecimal("30.50"));

        Price highPriority = createPrice(BRAND_ID, date.minusHours(3), date.plusHours(3),
                2, PRODUCT_ID, 1, new BigDecimal("25.45"));

        assertDoesNotThrow(() -> {
            Price result = service.selectApplicablePrice(
                    List.of(lowPriority, highPriority), date, PRODUCT_ID, BRAND_ID);

            assertAll(
                    () -> assertEquals(1, result.getPriority()),
                    () -> assertEquals(highPriority, result)
            );
        });
    }

    @Test
    @DisplayName("Lanza PriceNotFoundException si ninguna tarifa es aplicable en la fecha")
    void shouldThrowExceptionWhenNoPriceIsApplicable() {
        LocalDateTime now = LocalDateTime.now();

        Price price = createPrice(BRAND_ID, now.minusDays(2), now.minusDays(1),
                1, PRODUCT_ID, 0, new BigDecimal("19.99"));

        assertThrows(
                PriceNotFoundException.class,
                () -> service.selectApplicablePrice(List.of(price), now, PRODUCT_ID, BRAND_ID)
        );
    }

    @Test
    @DisplayName("Lanza PriceNotFoundException cuando la lista de precios está vacía")
    void shouldThrowExceptionWhenListIsEmpty() {
        LocalDateTime now = LocalDateTime.now();

        assertThrows(
                PriceNotFoundException.class,
                () -> service.selectApplicablePrice(List.of(), now, PRODUCT_ID, BRAND_ID)
        );
    }

    @Test
    @DisplayName("Devuelve uno de los precios si hay empate de prioridad")
    void shouldReturnAnyIfPrioritiesAreEqual() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);

        Price price1 = createPrice(BRAND_ID, date.minusHours(1), date.plusHours(1),
                1, PRODUCT_ID, 1, new BigDecimal("30.00"));

        Price price2 = createPrice(BRAND_ID, date.minusHours(2), date.plusHours(2),
                2, PRODUCT_ID, 1, new BigDecimal("40.00"));

        assertDoesNotThrow(() -> {
            Price result = service.selectApplicablePrice(
                    List.of(price1, price2), date, PRODUCT_ID, BRAND_ID);

            assertAll(
                    () -> assertEquals(1, result.getPriority()),
                    () -> assertTrue(result.equals(price1) || result.equals(price2))
            );
        });
    }

    @Test
    @DisplayName("Incluye tarifas aplicables exactamente en los bordes de fecha")
    void shouldIncludePriceAtStartAndEndDate() {
        LocalDateTime start = LocalDateTime.of(2020, 6, 14, 10, 0);
        LocalDateTime end = LocalDateTime.of(2020, 6, 14, 20, 0);

        Price price = createPrice(BRAND_ID, start, end, 1, PRODUCT_ID, 0,
                new BigDecimal("20.00"));

        List<Price> prices = List.of(price);

        assertAll(
                () -> assertDoesNotThrow(() -> {
                    Price result = service.selectApplicablePrice(prices, start, PRODUCT_ID, BRAND_ID);
                    assertEquals(price, result);
                }),
                () -> assertDoesNotThrow(() -> {
                    Price result = service.selectApplicablePrice(prices, end, PRODUCT_ID, BRAND_ID);
                    assertEquals(price, result);
                })
        );
    }
}
