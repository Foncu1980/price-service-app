package com.bcnc.ecommerce.priceservice.adapter.web;

import com.bcnc.ecommerce.priceservice.adapter.web.dto.PriceErrorResponse;
import com.bcnc.ecommerce.priceservice.domain.exception.PriceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 */
@DisplayName("GlobalExceptionHandler unit tests")
class GlobalExceptionHandlerTest {

    private static final long PRODUCT_ID = 99999L;
    private static final long BRAND_ID   = 1L;

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("handlePriceNotFound → devuelve 404 con mensaje detallado")
    void handlePriceNotFound_returnsNotFound() {

        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        PriceNotFoundException ex = new PriceNotFoundException(PRODUCT_ID, BRAND_ID, applicationDate);

        ResponseEntity<PriceErrorResponse> response = handler.handlePriceNotFound(ex);
        PriceErrorResponse body              = response.getBody();

        assertAll("PriceNotFound response",
                () -> assertEquals(404, response.getStatusCode().value()),
                () -> assertNotNull(body),
                () -> assertEquals(404, body.status()),
                () -> assertEquals("Not Found", body.error()),
                () -> assertTrue(body.message().contains("producto " + PRODUCT_ID)),
                () -> assertTrue(body.message().contains("cadena " + BRAND_ID)),
                () -> assertTrue(body.message().contains("2020-06-14T10:00"))
        );
    }

    @Test
    @DisplayName("handleMissingParams → devuelve 400 e indica parámetro faltante")
    void handleMissingParams_returnsBadRequest() {

        MissingServletRequestParameterException ex =
                new MissingServletRequestParameterException("productId", "Long");

        ResponseEntity<PriceErrorResponse> response = handler.handleMissingParams(ex);
        PriceErrorResponse body = response.getBody();

        assertAll("Missing parameter response",
                () -> assertEquals(400, response.getStatusCode().value()),
                () -> assertNotNull(body),
                () -> assertEquals(400, body.status()),
                () -> assertEquals("Bad Request", body.error()),
                () -> assertTrue(body.message().contains("productId"))
        );
    }

    @Test
    @DisplayName("handleIllegalArgument → devuelve 400 con mensaje original")
    void handleIllegalArgument_returnsBadRequest() {

        IllegalArgumentException ex = new IllegalArgumentException("invalid argument");

        ResponseEntity<PriceErrorResponse> response = handler.handleIllegalArgument(ex);
        PriceErrorResponse body = response.getBody();

        assertAll("IllegalArgument response",
                () -> assertEquals(400, response.getStatusCode().value()),
                () -> assertNotNull(body),
                () -> assertEquals("invalid argument", body.message())
        );
    }

    @Test
    @DisplayName("handleConstraintViolation → devuelve 400 con detalle de validación")
    void handleConstraintViolation_returnsBadRequest() {

        ConstraintViolationException ex =
                new ConstraintViolationException("validation failed", Collections.emptySet());

        ResponseEntity<PriceErrorResponse> response = handler.handleConstraintViolation(ex);
        PriceErrorResponse body = response.getBody();

        assertAll("ConstraintViolation response",
                () -> assertEquals(400, response.getStatusCode().value()),
                () -> assertNotNull(body),
                () -> assertEquals("validation failed", body.message())
        );
    }

    @Test
    @DisplayName("handleTypeMismatch → devuelve 400 indicando parámetro incorrecto")
    void handleTypeMismatch_returnsBadRequest() {

        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("productId");

        ResponseEntity<PriceErrorResponse> response = handler.handleTypeMismatch(ex);
        PriceErrorResponse body = response.getBody();

        assertAll("TypeMismatch response",
                () -> assertEquals(400, response.getStatusCode().value()),
                () -> assertNotNull(body),
                () -> assertTrue(body.message().contains("productId"))
        );
    }

    @Test
    @DisplayName("handleGenericException → devuelve 500 con mensaje genérico")
    void handleGenericException_returnsInternalServerError() {

        RuntimeException ex = new RuntimeException("unexpected");

        ResponseEntity<PriceErrorResponse> response = handler.handleGenericException(ex);
        PriceErrorResponse body = response.getBody();

        assertAll("Generic error response",
                () -> assertEquals(500, response.getStatusCode().value()),
                () -> assertNotNull(body),
                () -> assertEquals("Error interno del servidor", body.message())
        );
    }
}
