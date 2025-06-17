package com.bcnc.ecommerce.priceservice.application.exception;

import java.time.LocalDateTime;

/**
 * Excepción que se lanza cuando no se encuentra ningún precio aplicable
 * para un producto, una marca y una fecha concretos.
 */
public class PriceNotFoundException extends RuntimeException
{
    /**
     * Crea una nueva excepción indicando que no se encontró precio aplicable.
     *
     * @param productId       ID del producto buscado.
     * @param brandId         ID de la marca.
     * @param applicationDate Fecha de aplicación del precio.
     */
    public PriceNotFoundException(Long productId, Long brandId, LocalDateTime applicationDate)
    {
        super("No se encontró un precio para el producto " + productId + ", marca " + brandId +
                " en la fecha " + applicationDate);
    }
}