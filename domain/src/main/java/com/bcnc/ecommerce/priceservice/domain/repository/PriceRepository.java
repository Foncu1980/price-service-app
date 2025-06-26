package com.bcnc.ecommerce.priceservice.domain.repository;

import com.bcnc.ecommerce.priceservice.domain.model.Price;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Puerto de salida del dominio que define las operaciones para
 * acceder a los precios almacenados.
 * <p>
 * Esta interfaz debe ser implementada por un adaptador de infraestructura
 * que proporcione acceso a una fuente de datos.
 * </p>
 */
public interface PriceRepository
{
    /**
     * Recupera los precios aplicables para un producto y cadena en una fecha determinada.
     * <p>
     * La lógica de negocio que determina cuál es el precio final a aplicar
     * (por ejemplo, el de mayor prioridad) se gestiona en la capa de dominio.
     *
     * @param applicationDate fecha de aplicación.
     * @param productId       identificador del producto.
     * @param brandId         identificador de la cadena.
     * @return Lista de {@link Price} candidatos.
     */
    List<Price> findApplicablePrices(LocalDateTime applicationDate, Long productId, Long brandId);
}
