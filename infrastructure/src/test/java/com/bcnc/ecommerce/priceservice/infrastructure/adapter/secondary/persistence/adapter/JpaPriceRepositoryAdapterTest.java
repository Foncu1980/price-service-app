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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        Price price = new Price(PRODUCT_ID, date, date.plusHours(2), 2, PRODUCT_ID, 1, new BigDecimal("25.45"), "EUR");

        when(priceJpaRepository.findApplicablePrices(eq(date), eq(PRODUCT_ID), eq(BRAND_ID)))
                .thenReturn(List.of(entity));
        when(priceMapper.toDomain(entity)).thenReturn(price);

        List<Price> result = adapter.findApplicablePrices(date, PRODUCT_ID, BRAND_ID);

        assertEquals(1, result.size());
        assertEquals(price, result.get(0));

        verify(priceJpaRepository, times(1)).findApplicablePrices(eq(date), eq(PRODUCT_ID), eq(BRAND_ID));
        verify(priceMapper, times(1)).toDomain(entity);
    }

    @Test
    @DisplayName("Devuelve lista vacía cuando no hay resultados")
    void testFindTopPriceWhenEmpty()
    {
        LocalDateTime date = LocalDateTime.of(2020, 6, 13, 10, 0);

        when(priceJpaRepository.findApplicablePrices(eq(date), eq(PRODUCT_ID), eq(BRAND_ID)))
                .thenReturn(List.of());

        List<Price> result = adapter.findApplicablePrices(date, PRODUCT_ID, BRAND_ID);

        assertTrue(result.isEmpty());

        verify(priceJpaRepository, times(1)).findApplicablePrices(eq(date), eq(PRODUCT_ID), eq(BRAND_ID));
        verify(priceMapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("Mapea correctamente múltiples entidades devueltas por el repositorio")
    void testFindAllPricesWhenMultipleResultsExist() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);

        // Entidades diferenciadas (puedes setear cualquier campo para distinguirlas si tienen equals heredado)
        PriceEntity entity1 = new PriceEntity(BRAND_ID,date,date.plusHours(1),1,
                PRODUCT_ID,1,new BigDecimal("25.45"),"EUR");

        PriceEntity entity2 = new PriceEntity(BRAND_ID,date.minusHours(1),date.plusHours(2),2,
                PRODUCT_ID,0,new BigDecimal("35.55"),"EUR");

        // Simulación de respuestas mapeadas
        Price price1 = new Price(BRAND_ID, date, date.plusHours(1), 1, PRODUCT_ID, 1, new BigDecimal("25.45"), "EUR");
        Price price2 = new Price(BRAND_ID, date.minusHours(1), date.plusHours(2), 2, PRODUCT_ID, 0, new BigDecimal("35.55"), "EUR");

        // Simula que se devuelven ambas entidades desde la base de datos
        when(priceJpaRepository.findApplicablePrices(eq(date), eq(PRODUCT_ID), eq(BRAND_ID)))
                .thenReturn(List.of(entity1, entity2));

        // Mapea cada entidad al objeto correspondiente
        when(priceMapper.toDomain(entity1)).thenReturn(price1);
        when(priceMapper.toDomain(entity2)).thenReturn(price2);

        // Ejecuta
        List<Price> result = adapter.findApplicablePrices(date, PRODUCT_ID, BRAND_ID);

        // Comprueba que se han mapeado correctamente
        assertEquals(2, result.size());
        assertTrue(result.contains(price1));
        assertTrue(result.contains(price2));

        verify(priceJpaRepository).findApplicablePrices(date, PRODUCT_ID, BRAND_ID);
        verify(priceMapper).toDomain(entity1);
        verify(priceMapper).toDomain(entity2);
        verifyNoMoreInteractions(priceMapper);
    }

    @Test
    @DisplayName("Lanza excepción si falla el mapeo de entidad")
    void testMapperThrowsException() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);
        PriceEntity faultyEntity = new PriceEntity();

        when(priceJpaRepository.findApplicablePrices(eq(date), eq(PRODUCT_ID), eq(BRAND_ID)))
                .thenReturn(List.of(faultyEntity));
        when(priceMapper.toDomain(faultyEntity)).thenThrow(new RuntimeException("Mapping error"));

        assertThrows(RuntimeException.class, () ->
                adapter.findApplicablePrices(date, PRODUCT_ID, BRAND_ID)
        );
    }

}
