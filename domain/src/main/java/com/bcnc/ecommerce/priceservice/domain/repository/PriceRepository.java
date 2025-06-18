package com.bcnc.ecommerce.priceservice.domain.repository;

import com.bcnc.ecommerce.priceservice.domain.model.Price;
import java.time.LocalDateTime;
import java.util.Optional;

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
 * Recupera el precio más prioritario aplicable a un producto y cadena en una fecha dada.
 * Si existen varios precios válidos para el mismo rango, se devuelve el de mayor prioridad.
 *
 * @param applicationDate fecha de aplicación
 * @param productId identificador del producto
 * @param brandId identificador de la cadena
 * @return un {@link Price} con mayor prioridad, si existe
 */
    Optional<Price> findTopPriceByProductIdAndBrandIdAndDate(LocalDateTime applicationDate, Long productId, Long brandId);
}
