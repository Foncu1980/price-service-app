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
 * <p>Define una consulta personalizada para recuperar todas las tarifas
 * aplicablesen función del producto, la cadena y la fecha de aplicación.</p>
 *
 * <p>La lógica de selección del precio final se realiza en la capa
 * de dominio.</p>
 */
@Repository
public interface PriceJpaRepository extends JpaRepository<PriceEntity, Long> {
    /**
     * Busca las tarifas que aplican a un producto y cadena en una fecha
     * específica.
     *
     * @param applicationDate  fecha en la que debe aplicarse la tarifa.
     * @param productId        identificador del producto.
     * @param brandId          identificador de la cadena.
     * @param pageable         objeto de paginación.
     * @return lista de tarifas aplicables ordenadas por prioridad
     * (de mayor a menor).
     */
    @Query("""
    SELECT p FROM PriceEntity p
    WHERE p.productId = :productId
      AND p.brandId = :brandId
      AND :applicationDate BETWEEN p.startDate AND p.endDate
      ORDER BY p.priority DESC
""")
    List<PriceEntity> findApplicablePrices(
            @Param("applicationDate") LocalDateTime applicationDate,
            @Param("productId") Long productId,
            @Param("brandId") Long brandId,
            Pageable pageable);
}
