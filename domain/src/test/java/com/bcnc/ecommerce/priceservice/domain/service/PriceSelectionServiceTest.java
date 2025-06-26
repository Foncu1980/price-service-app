package com.bcnc.ecommerce.priceservice.domain.service;

import com.bcnc.ecommerce.priceservice.domain.model.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PriceSelectionServiceTest {

    private PriceSelectionService service;

    @BeforeEach
    void setUp() {
        service = new PriceSelectionService();
    }

    @Test
    @DisplayName("Devuelve el precio cuando solo hay uno aplicable")
    void shouldReturnSingleApplicablePrice() {
        LocalDateTime now = LocalDateTime.now();
        Price price = new Price(1L, now.minusHours(1), now.plusHours(1), 1, 100L, 0, new BigDecimal("20.00"), "EUR");

        Optional<Price> result = service.selectApplicablePrice(List.of(price), now);

        assertTrue(result.isPresent());
        assertEquals(price, result.get());
    }

    @Test
    @DisplayName("Devuelve el precio con mayor prioridad entre múltiples aplicables")
    void shouldReturnHighestPriorityPrice() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);

        Price lowPriority = new Price(1L, date.minusHours(2), date.plusHours(2), 1, 35455L, 0, new BigDecimal("30.50"), "EUR");
        Price highPriority = new Price(1L, date.minusHours(3), date.plusHours(3), 2, 35455L, 1, new BigDecimal("25.45"), "EUR");

        Optional<Price> result = service.selectApplicablePrice(List.of(lowPriority, highPriority), date);

        assertTrue(result.isPresent());
        assertEquals(highPriority, result.get());
    }

    @Test
    @DisplayName("Devuelve vacío si ninguna tarifa es aplicable en la fecha")
    void shouldReturnEmptyWhenNoPriceIsApplicable() {
        LocalDateTime now = LocalDateTime.now();
        Price price = new Price(1L, now.minusDays(2), now.minusDays(1), 1, 100L, 0, new BigDecimal("19.99"), "EUR");

        Optional<Price> result = service.selectApplicablePrice(List.of(price), now);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Devuelve vacío si la lista está vacía")
    void shouldReturnEmptyWhenListIsEmpty() {
        LocalDateTime now = LocalDateTime.now();

        Optional<Price> result = service.selectApplicablePrice(List.of(), now);

        assertTrue(result.isEmpty());
    }
}
