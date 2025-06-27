package com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.adapter;

import com.bcnc.ecommerce.priceservice.domain.model.Price;
import com.bcnc.ecommerce.priceservice.domain.repository.PriceRepository;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.mapper.PriceMapper;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.repository.PriceJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Adaptador de infraestructura que implementa el repositorio de
 * dominio {@link PriceRepository} usando un repositorio JPA
 * sobre una base de datos H2 en memoria.
 * <p>
 * Este adaptador actúa como puerto de salida para acceder a los precios
 * persistidos, y se encarga de mapear las entidades JPA a objetos del dominio.
 * </p>
 */
@Component
public class JpaPriceRepositoryAdapter implements PriceRepository {
    /** Repositorio JPA de Spring para acceder a la entidad persistente. */
    private final PriceJpaRepository priceJpaRepository;
    /** Componente encargado del mapeo entre entidad persistente y
     * modelo de dominio. */
    private final PriceMapper priceMapper;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param jpaRepository repositorio JPA que accede a la base de datos.
     * @param mapper        componente de mapeo entre entidad y
     *                           modelo de dominio.
     */
    public JpaPriceRepositoryAdapter(final PriceJpaRepository jpaRepository,
                                     final PriceMapper mapper) {
        this.priceJpaRepository = jpaRepository;
        this.priceMapper = mapper;
    }

    /**
     * Recupera todas las tarifas que aplican al producto y cadena
     * especificados en la fecha proporcionada.
     *
     * <p>La lógica para seleccionar el precio final más adecuado se aplica
     * en el dominio.</p>
     *
     * @param applicationDate fecha de aplicación para la búsqueda.
     * @param productId       identificador del producto.
     * @param brandId         identificador de la cadena.
     * @return lista de {@link Price} aplicables.
     */
    @Override
    public List<Price> findApplicablePrices(
            final LocalDateTime applicationDate,
            final Long productId,
            final Long brandId) {

        return priceJpaRepository
                .findApplicablePrices(applicationDate, productId, brandId)
                .stream()
                .map(priceMapper::toDomain)
                .toList();
    }
}
