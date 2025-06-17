package com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.repository;


import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.entity.PriceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para acceder a la entidad {@link PriceEntity}.
 *
 * <p>Define una consulta personalizada para obtener la tarifa aplicable con mayor prioridad
 * en función del producto, la marca y la fecha de aplicación.</p>
 */
@Repository
public interface PriceJpaRepository extends JpaRepository<PriceEntity, Long>
{
    /**
     * Busca las tarifas que aplican a un producto y marca en una fecha específica,
     * ordenadas por prioridad descendente.
     *
     * <p>Se utiliza {@link Pageable} para limitar el número de resultados (usualmente 1).</p>
     *
     * @param applicationDate  fecha en la que debe aplicarse la tarifa.
     * @param productId        identificador del producto.
     * @param brandId          identificador de la marca.
     * @param pageable         objeto para limitar y ordenar los resultados.
     * @return lista de tarifas aplicables ordenadas por prioridad (de mayor a menor).
     */
    @Query("""
    SELECT p FROM PriceEntity p
    WHERE p.productId = :productId
      AND p.brandId = :brandId
      AND :applicationDate BETWEEN p.startDate AND p.endDate
    ORDER BY p.priority DESC
""")
    List<PriceEntity> findTopApplicablePrice(
            @Param("applicationDate") LocalDateTime applicationDate,
            @Param("productId") Long productId,
            @Param("brandId") Long brandId,
            Pageable pageable);
}
