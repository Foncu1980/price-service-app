package com.bcnc.ecommerce.priceservice.adapter.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO de error para respuestas HTTP fallidas en la API de precios.
 * Incluye información sobre el estado HTTP, mensaje de error y marca temporal.
 *
 * <pre>
 * {
 *   "timestamp": "2025-06-17T18:40:00",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "No se encontró un precio para el producto 99999"
 * }
 * </pre>
 */
public record PriceErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message
) {}