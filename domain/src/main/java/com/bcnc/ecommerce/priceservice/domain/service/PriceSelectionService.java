package com.bcnc.ecommerce.priceservice.domain.service;

import com.bcnc.ecommerce.priceservice.domain.exception.PriceNotFoundException;
import com.bcnc.ecommerce.priceservice.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio de dominio encargado de aplicar las reglas de negocio
 * para seleccionar el precio correcto por su fecha y prioridad.
 */
public class PriceSelectionService {

    /** Logger. */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(PriceSelectionService.class);

    /**
     * Selecciona el precio aplicable para la fecha dada
     * (el de mayor prioridad, si hay más de uno).
     *
     * @param prices          lista de precios candidatos.
     * @param applicationDate fecha de aplicación.
     * @param productId       identificador del producto
     * @param brandId         identificador de la cadena.
     * @return precio aplicable.
     * @throws PriceNotFoundException si no se encuentra ningún
     * precio aplicable.
     */
    public Price selectApplicablePrice(
            final List<Price> prices,
            final LocalDateTime applicationDate,
            final Long productId,
            final Long brandId) {

        // Aunque el repositorio ya filtra precios a nivel de base de datos,
        // se aplica un filtrado adicional aquí para reforzar la lógica de
        // negocio. Esto permite:
        // - Encapsular completamente el comportamiento en el dominio.
        // - Garantizar que la lógica se aplica de forma coherente,
        //   independientemente de la fuente de datos (repositorio,
        //   test, mock, etc.).
        // - Facilitar futuras extensiones del criterio de selección.
        // - Aumentar la robustez y consistencia del sistema.

        // Para mayor robustez.
        Objects.requireNonNull(applicationDate,
                "applicationDate no puede ser nula.");
        Objects.requireNonNull(productId, "productId no puede ser nulo.");
        Objects.requireNonNull(brandId, "brandId no puede ser nulo.");
        Objects.requireNonNull(prices, "prices no puede ser nulo.");

        return prices.stream()
                .filter(price -> price.isApplicableOn(applicationDate))
                .max(Comparator.comparingInt(Price::getPriority))
                .orElseThrow(() -> logAndThrow(productId, brandId,
                        applicationDate));
    }

    private PriceNotFoundException logAndThrow(final Long productId,
                                               final Long brandId,
                                               final LocalDateTime date) {
        LOGGER.warn("No se encontró ningún precio aplicable para productId={},"
                        + " brandId={} en la fecha={}",
                productId, brandId, date);

        return new PriceNotFoundException(productId, brandId, date);
    }
}
