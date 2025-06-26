package com.bcnc.ecommerce.priceservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.bcnc.ecommerce.priceservice.application.impl.PriceServiceImpl;
import com.bcnc.ecommerce.priceservice.application.exception.PriceNotFoundException;
import com.bcnc.ecommerce.priceservice.domain.model.Price;
import com.bcnc.ecommerce.priceservice.domain.repository.PriceRepository;

import com.bcnc.ecommerce.priceservice.domain.service.PriceSelectionService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

        Price price = new Price(brandId, date.minusHours(1), date.plusHours(1), 1, productId, 0,
                new BigDecimal("35.50"), "EUR");

        List<Price> prices = List.of(price);

        when(priceRepository.findApplicablePrices(date, productId, brandId))
                .thenReturn(prices);
        when(priceSelectionService.selectApplicablePrice(prices, date))
                .thenReturn(Optional.of(price));

        Price result = priceService.findApplicablePrice(date, productId, brandId);

        assertNotNull(result);
        assertEquals(new BigDecimal("35.50"), result.getPrice());
        assertEquals(productId, result.getProductId());
        assertEquals(brandId, result.getBrandId());
        assertEquals("EUR", result.getCurr());
        assertEquals(0, result.getPriority());

        verify(priceRepository, times(1)).findApplicablePrices(date, productId, brandId);
        verify(priceSelectionService, times(1)).selectApplicablePrice(prices, date);
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
        when(priceSelectionService.selectApplicablePrice(emptyList, date))
                .thenReturn(Optional.empty());

        assertThrows(PriceNotFoundException.class, () ->
                priceService.findApplicablePrice(date, productId, brandId)
        );

        verify(priceRepository, times(1)).findApplicablePrices(date, productId, brandId);
        verify(priceSelectionService, times(1)).selectApplicablePrice(emptyList, date);
    }
}
