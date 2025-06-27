package com.bcnc.ecommerce.priceservice.adapter.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para la API REST de consulta de precios.
 * <p>
 * Representa la tarifa final aplicable a un producto en un momento dado,
 * incluyendo información de validez, precio y moneda.
 * </p>
 *
 * <p>Este objeto se serializa como respuesta JSON.</p>
 *
 * <pre>
 * {
 *   "productId": 35455,
 *   "brandId": 1,
 *   "priceList": 2,
 *   "startDate": "2020-06-14T15:00:00",
 *   "endDate": "2020-06-14T18:30:00",
 *   "price": 25.45,
 *   "curr": "EUR"
 * }
 * </pre>
 *
 * @param productId identificador del producto
 * @param brandId identificador de la cadena
 * @param priceList identificador de la tarifa aplicada
 * @param startDate fecha y hora de inicio de validez
 * @param endDate fecha y hora de fin de validez
 * @param price precio final
 * @param curr moneda del precio
 */
@Schema(
        name = "PriceResponse",
        requiredProperties = {
                "productId", "brandId", "priceList", "startDate", "endDate",
                "price", "curr"
        }
)
public record PriceResponse(
        @Schema(description = "Identificador del producto", example = "35455")
        Long productId,

        @Schema(description = "Identificador de la cadena", example = "1")
        Long brandId,

        @Schema(description = "Identificador de la tarifa", example = "2")
        Integer priceList,

        @Schema(description = "Fecha de inicio de validez",
                example = "2020-06-14T15:00:00")
        LocalDateTime startDate,

        @Schema(description = "Fecha de fin de validez",
                example = "2020-06-14T18:30:00")
        LocalDateTime endDate,

        @Schema(description = "Precio final", example = "25.45")
        BigDecimal price,

        @Schema(description = "Moneda", example = "EUR")
        String curr
) { }
