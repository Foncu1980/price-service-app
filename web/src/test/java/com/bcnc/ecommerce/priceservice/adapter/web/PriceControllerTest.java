package com.bcnc.ecommerce.priceservice.adapter.web;

import com.bcnc.ecommerce.priceservice.adapter.web.dto.PriceResponse;
import com.bcnc.ecommerce.priceservice.application.PriceService;
import com.bcnc.ecommerce.priceservice.domain.model.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PriceControllerUnitTest {

    private PriceService priceService;
    private PriceController controller;

    @BeforeEach
    void setUp() {
        priceService = mock(PriceService.class);
        controller = new PriceController(priceService);
    }

    @Test
    void getApplicablePrice_ReturnsMappedResponse() {
        // Given
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        Price mockPrice = new Price(brandId,date, date.plusHours(2), 1, productId, 1,
                new BigDecimal("25.45"), "EUR");

        when(priceService.findApplicablePrice(date, productId, brandId)).thenReturn(mockPrice);

        // When
        ResponseEntity<PriceResponse> response = controller.getApplicablePrice(date, productId, brandId);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        PriceResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(productId, body.productId());
        assertEquals(brandId, body.brandId());
        assertEquals(1, body.priceList());
        assertEquals(new BigDecimal("25.45"), body.price());
        assertEquals("EUR", body.curr());
        assertEquals(date, body.startDate());
        assertEquals(date.plusHours(2), body.endDate());

        verify(priceService).findApplicablePrice(date, productId, brandId);
    }
}
