package com.bcnc.ecommerce.priceservice.adapter.web.dto;

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
 */
public class PriceResponse
{
    public final Long productId;
    public final Long brandId;
    public final Integer priceList;
    public final LocalDateTime startDate;
    public final LocalDateTime endDate;
    public final BigDecimal price;
    public final String curr;

    /**
     * Constructor completo para inicializar todos los campos del DTO.
     *
     * @param productId Identificador del producto.
     * @param brandId Identificador de la cadena comercial.
     * @param priceList Identificador de la tarifa aplicable.
     * @param startDate Fecha de inicio de validez de la tarifa.
     * @param endDate Fecha de fin de validez de la tarifa.
     * @param price Precio a aplicar.
     * @param curr Código de moneda (ej: EUR).
     */
    public PriceResponse(Long productId, Long brandId, Integer priceList, LocalDateTime startDate,
                         LocalDateTime endDate, BigDecimal price, String curr)
    {
        this.productId = productId;
        this.brandId = brandId;
        this.priceList = priceList;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.curr = curr;
    }
}
