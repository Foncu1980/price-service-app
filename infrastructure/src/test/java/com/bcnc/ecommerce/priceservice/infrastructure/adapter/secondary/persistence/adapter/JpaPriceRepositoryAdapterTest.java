package com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.adapter;

import com.bcnc.ecommerce.priceservice.domain.model.Price;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.entity.PriceEntity;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.mapper.PriceMapper;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.repository.PriceJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaPriceRepositoryAdapterTest
{
    private PriceJpaRepository priceJpaRepository;
    private PriceMapper priceMapper;
    private JpaPriceRepositoryAdapter adapter;

    @BeforeEach
    void setUp()
    {
        priceJpaRepository = mock(PriceJpaRepository.class);
        priceMapper = mock(PriceMapper.class);
        adapter = new JpaPriceRepositoryAdapter(priceJpaRepository, priceMapper);
    }

    @Test
    @DisplayName("Devuelve Optional con Price cuando hay resultados")
    void testFindTopPriceWhenExists()
    {
        Long productId = 35455L;
        Long brandId = 1L;
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);

        PriceEntity entity = new PriceEntity();
        Price price = new Price(productId, date, date.plusHours(2), 2, productId, 1, new BigDecimal("25.45"), "EUR");

        when(priceJpaRepository.findApplicablePrices(eq(date), eq(productId), eq(brandId)))
                .thenReturn(List.of(entity));
        when(priceMapper.toDomain(entity)).thenReturn(price);

        List<Price> result = adapter.findApplicablePrices(date, productId, brandId);

        assertEquals(1, result.size());
        assertEquals(price, result.get(0));

        verify(priceJpaRepository, times(1)).findApplicablePrices(eq(date), eq(productId), eq(brandId));
        verify(priceMapper, times(1)).toDomain(entity);
    }

    @Test
    @DisplayName("Devuelve Optional.empty() cuando no hay resultados")
    void testFindTopPriceWhenEmpty()
    {
        Long productId = 35455L;
        Long brandId = 1L;
        LocalDateTime date = LocalDateTime.of(2020, 6, 13, 10, 0);

        when(priceJpaRepository.findApplicablePrices(eq(date), eq(productId), eq(brandId)))
                .thenReturn(List.of());

        List<Price> result = adapter.findApplicablePrices(date, productId, brandId);

        assertTrue(result.isEmpty());

        verify(priceJpaRepository, times(1)).findApplicablePrices(eq(date), eq(productId), eq(brandId));
        verify(priceMapper, never()).toDomain(any());
    }
}
