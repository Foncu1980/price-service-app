package com.bcnc.ecommerce.priceservice.application;

import com.bcnc.ecommerce.priceservice.domain.model.Price;
import java.time.LocalDateTime;

/**
 * Servicio de aplicación que orquesta la lógica de negocio relacionada
 * con la búsqueda de precios aplicables.
 * <p>
 * Esta interfaz define el contrato que deben cumplir los casos de uso
 * que permiten consultar precios en función de la fecha, el identificador
 * del producto y la cadena.
 * </p>
 */
public interface PriceService {
    /**
     * Recupera el precio aplicable para un producto y una cadena
     * en una fecha dada.
     *
     * @param applicationDate fecha de aplicación
     * @param productId       identificador del producto
     * @param brandId         identificador de la cadena
     * @return el precio correspondiente
     */
    Price findApplicablePrice(LocalDateTime applicationDate,
                              Long productId, Long brandId);
}

