package com.bcnc.ecommerce.priceservice.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.bcnc.ecommerce.priceservice.application.impl.PriceServiceImpl;
import com.bcnc.ecommerce.priceservice.domain.exception.PriceNotFoundException;
import com.bcnc.ecommerce.priceservice.domain.model.Price;
import com.bcnc.ecommerce.priceservice.domain.repository.PriceRepository;

import com.bcnc.ecommerce.priceservice.domain.service.PriceSelectionService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceServiceImplTest {

    private PriceRepository priceRepository;
    private PriceSelectionService priceSelectionService;
    private PriceServiceImpl priceService;

    @BeforeEach
    void setUp() {
        priceRepository = mock(PriceRepository.class);
        priceSelectionService = mock(PriceSelectionService.class);
        priceService = new PriceServiceImpl(priceRepository, priceSelectionService);
    }

    @Test
    @DisplayName("Devuelve tarifa con mayor prioridad entre varias coincidentes")
    void shouldReturnPriceWithHighestPriority() {
        Long productId = 35455L;
        Long brandId = 1L;
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);

        Price price = Price.builder()
                .brandId(brandId)
                .startDate(date.minusHours(1))
                .endDate(date.plusHours(1))
                .priceList(1)
                .productId(productId)
                .priority(0)
                .price(new BigDecimal("35.50"))
                .curr("EUR")
                .build();

        List<Price> prices = List.of(price);

        when(priceRepository.findApplicablePrices(date, productId, brandId))
                .thenReturn(prices);
        when(priceSelectionService.selectApplicablePrice(prices, date, productId, brandId))
                .thenReturn(price);

        Price result = priceService.findApplicablePrice(date, productId, brandId);

        assertNotNull(result);
        assertEquals(new BigDecimal("35.50"), result.getPrice());
        assertEquals(productId, result.getProductId());
        assertEquals(brandId, result.getBrandId());
        assertEquals("EUR", result.getCurr());
        assertEquals(0, result.getPriority());

        verify(priceRepository, times(1)).findApplicablePrices(date, productId, brandId);
        verify(priceSelectionService, times(1)).selectApplicablePrice(prices, date,
                productId, brandId);
    }

    @Test
    @DisplayName("Lanza PriceNotFoundException si no hay tarifas aplicables")
    void shouldThrowPriceNotFoundException() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 13, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        List<Price> emptyList = List.of();

        when(priceRepository.findApplicablePrices(date, productId, brandId))
                .thenReturn(emptyList);

        when(priceSelectionService.selectApplicablePrice(
                eq(emptyList), eq(date), eq(productId), eq(brandId)
        )).thenThrow(new PriceNotFoundException(productId, brandId, date));

        PriceNotFoundException exception = assertThrows(
                PriceNotFoundException.class,
                () -> priceService.findApplicablePrice(date, productId, brandId)
        );

        String message = exception.getMessage();

        assertAll(
                () -> assertTrue(message.contains(productId.toString())),
                () -> assertTrue(message.contains(brandId.toString())),
                () -> assertTrue(message.contains(date.toString()))
        );

        verify(priceRepository, times(1)).findApplicablePrices(date, productId, brandId);
        verify(priceSelectionService, times(1))
                .selectApplicablePrice(emptyList, date, productId, brandId);
    }

    @DisplayName("Selecciona correctamente el precio con mayor prioridad")
    @Test
    void shouldSelectPriceWithHighestPriority() {
        Long productId = 35455L;
        Long brandId = 1L;
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 18, 0);

        Price lowPriority = Price.builder()
                .brandId(brandId)
                .startDate(date.minusHours(1))
                .endDate(date.plusHours(2))
                .priceList(1)
                .productId(productId)
                .priority(0)
                .price(new BigDecimal("30.00"))
                .curr("EUR")
                .build();

        Price highPriority = Price.builder().
                brandId(brandId)
                .startDate(date.minusHours(2))
                .endDate(date.plusHours(1))
                .priceList(2)
                .productId(productId)
                .priority(1)
                .price(new BigDecimal("50.00"))
                .curr("EUR")
                .build();

        List<Price> prices = List.of(lowPriority, highPriority);

        when(priceRepository.findApplicablePrices(date, productId, brandId))
                .thenReturn(prices);
        when(priceSelectionService.selectApplicablePrice(prices, date, productId, brandId))
                .thenReturn(highPriority);

        Price result = priceService.findApplicablePrice(date, productId, brandId);

        assertEquals(highPriority.getPrice(), result.getPrice());
        assertEquals(1, result.getPriority());

        verify(priceRepository).findApplicablePrices(date, productId, brandId);
        verify(priceSelectionService).selectApplicablePrice(prices, date, productId, brandId);
    }
}
