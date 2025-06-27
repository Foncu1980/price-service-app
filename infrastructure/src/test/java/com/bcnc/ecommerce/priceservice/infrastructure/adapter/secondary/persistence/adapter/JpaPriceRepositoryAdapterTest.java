package com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

import com.bcnc.ecommerce.priceservice.domain.model.Price;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.entity.PriceEntity;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.mapper.PriceMapper;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.repository.PriceJpaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

class JpaPriceRepositoryAdapterTest
{
    private static final Long PRODUCT_ID = 35455L;
    private static final Long BRAND_ID = 1L;

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
    @DisplayName("Devuelve lista con un Price cuando hay resultados")
    void testFindTopPriceWhenExists()
    {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);

        PriceEntity entity = new PriceEntity();
        Price price = createPrice(date, date.plusHours(1), 1, 1, new BigDecimal("25.45"));

        when(priceJpaRepository.findApplicablePrices(eq(date), eq(PRODUCT_ID), eq(BRAND_ID),
                any(Pageable.class))).thenReturn(List.of(entity));
        when(priceMapper.toDomain(entity)).thenReturn(price);

        List<Price> result = adapter.findApplicablePrices(date, PRODUCT_ID, BRAND_ID);

        assertEquals(1, result.size());
        assertEquals(price, result.get(0));

        verify(priceJpaRepository, times(1)).findApplicablePrices(eq(date),
                eq(PRODUCT_ID), eq(BRAND_ID), any(Pageable.class));
        verify(priceMapper, times(1)).toDomain(entity);
    }

    @Test
    @DisplayName("Devuelve lista vacía cuando no hay resultados")
    void testFindTopPriceWhenEmpty()
    {
        LocalDateTime date = LocalDateTime.of(2020, 6, 13, 10, 0);

        when(priceJpaRepository.findApplicablePrices(eq(date), eq(PRODUCT_ID), eq(BRAND_ID),
                any(Pageable.class))).thenReturn(List.of());

        List<Price> result = adapter.findApplicablePrices(date, PRODUCT_ID, BRAND_ID);

        assertTrue(result.isEmpty());

        verify(priceJpaRepository, times(1)).findApplicablePrices(eq(date),
                eq(PRODUCT_ID), eq(BRAND_ID), any(Pageable.class));
        verify(priceMapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("Lanza excepción si falla el mapeo de entidad")
    void testMapperThrowsException() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);
        PriceEntity faultyEntity = new PriceEntity();

        when(priceJpaRepository.findApplicablePrices(eq(date), eq(PRODUCT_ID), eq(BRAND_ID),
                any(Pageable.class))).thenReturn(List.of(faultyEntity));
        when(priceMapper.toDomain(faultyEntity)).thenThrow(new RuntimeException("Mapping error"));

        assertThrows(RuntimeException.class, () ->
                adapter.findApplicablePrices(date, PRODUCT_ID, BRAND_ID)
        );
    }

    private Price createPrice(
            final LocalDateTime start,
            final LocalDateTime end,
            final Integer priceList,
            final Integer priority,
            final BigDecimal price
    ) {
        return Price.builder()
                .brandId(BRAND_ID)
                .startDate(start)
                .endDate(end)
                .priceList(priceList)
                .productId(PRODUCT_ID)
                .priority(priority)
                .price(price)
                .curr("EUR")
                .build();
    }

    private PriceEntity createPriceEntity(
            final LocalDateTime start,
            final LocalDateTime end,
            final Integer priceList,
            final Integer priority,
            final BigDecimal price
    ) {
        return PriceEntity.builder()
                .brandId(BRAND_ID)
                .startDate(start)
                .endDate(end)
                .priceList(priceList)
                .productId(PRODUCT_ID)
                .priority(priority)
                .price(price)
                .curr("EUR")
                .build();
    }
}
