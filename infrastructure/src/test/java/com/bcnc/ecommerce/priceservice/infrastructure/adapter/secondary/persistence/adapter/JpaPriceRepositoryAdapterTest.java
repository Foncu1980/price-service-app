package com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.adapter;

import com.bcnc.ecommerce.priceservice.domain.model.Price;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.entity.PriceEntity;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.mapper.PriceMapper;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.repository.PriceJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.PageRequest;

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

        when(priceJpaRepository.findTopApplicablePrice(eq(date), eq(productId), eq(brandId), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(List.of(entity));
        when(priceMapper.toDomain(entity)).thenReturn(price);

        Optional<Price> result = adapter.findTopPriceByProductIdAndBrandIdAndDate(date, productId, brandId);

        assertTrue(result.isPresent());
        assertEquals(price, result.get());

        verify(priceJpaRepository, times(1)).findTopApplicablePrice(eq(date), eq(productId), eq(brandId),ArgumentMatchers.any(PageRequest.class));
        verify(priceMapper, times(1)).toDomain(entity);
    }

    @Test
    @DisplayName("Devuelve Optional.empty() cuando no hay resultados")
    void testFindTopPriceWhenEmpty()
    {
        Long productId = 35455L;
        Long brandId = 1L;
        LocalDateTime date = LocalDateTime.of(2020, 6, 13, 10, 0);

        when(priceJpaRepository.findTopApplicablePrice(eq(date), eq(productId), eq(brandId), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(List.of());

        Optional<Price> result = adapter.findTopPriceByProductIdAndBrandIdAndDate(date, productId, brandId);

        assertFalse(result.isPresent());
        verify(priceJpaRepository, times(1)).findTopApplicablePrice(eq(date), eq(productId), eq(brandId), ArgumentMatchers.any(PageRequest.class));
        verify(priceMapper, never()).toDomain(any());
    }
}
