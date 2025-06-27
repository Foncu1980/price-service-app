package com.bcnc.ecommerce.priceservice.domain.exception;

import java.time.LocalDateTime;

/**
 * Excepción que se lanza cuando no se encuentra ningún precio aplicable
 * para un producto, una cadena y una fecha concretos.
 */
public class PriceNotFoundException extends RuntimeException {
    /**
     * Crea una nueva excepción indicando que no se encontró precio aplicable.
     *
     * @param productId       ID del producto buscado.
     * @param brandId         ID de la cadena.
     * @param applicationDate Fecha de aplicación del precio.
     */
    public PriceNotFoundException(final Long productId,
                                  final Long brandId,
                                  final LocalDateTime applicationDate) {
        super("No se encontró un precio para el producto " + productId
                + ", cadena " + brandId + " en la fecha " + applicationDate);
    }
}
