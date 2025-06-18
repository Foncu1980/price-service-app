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

        @Schema(
                description = "Fecha y hora del error",
                example = "2025-06-17T18:40:00",
                type = "string",
                format = "date-time"
        )
        LocalDateTime timestamp,

        @Schema(
                description = "Código de estado HTTP",
                example = "404"
        )
        int status,

        @Schema(
                description = "Nombre del error HTTP",
                example = "Not Found"
        )
        String error,

        @Schema(
                description = "Mensaje detallado del error",
                example = "No se encontró un precio para el producto 99999"
        )
        String message

) {}