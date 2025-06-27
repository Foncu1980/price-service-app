package com.bcnc.ecommerce.priceservice.adapter.web.dto;

import java.time.LocalDateTime;

/**
 * Representa la respuesta de error estándar para la API de precios.
 * <p>
 * Este DTO se utiliza en respuestas HTTP fallidas para proporcionar
 * información estructurada sobre el error ocurrido.
 * </p>
 *
 * <p>Ejemplo de respuesta JSON:</p>
 * <pre>
 * {
 *   "timestamp": "2025-06-17T18:40:00",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "No se encontró un precio para el producto 99999"
 * }
 * </pre>
 *
 * @param timestamp fecha y hora del error
 * @param status código de estado HTTP
 * @param error descripción corta del error
 * @param message mensaje explicativo detallado
 */
public record PriceErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message
) { }
