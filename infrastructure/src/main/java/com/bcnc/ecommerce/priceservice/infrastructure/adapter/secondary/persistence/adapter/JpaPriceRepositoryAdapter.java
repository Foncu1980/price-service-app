package com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.adapter;

import com.bcnc.ecommerce.priceservice.domain.model.Price;
import com.bcnc.ecommerce.priceservice.domain.repository.PriceRepository;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.mapper.PriceMapper;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.repository.PriceJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Adaptador de infraestructura que implementa el repositorio de dominio {@link PriceRepository}
 * usando un repositorio JPA sobre una base de datos H2 en memoria.
 * <p>
 * Este adaptador actúa como puerto de salida para acceder a los precios persistidos,
 * y se encarga de mapear las entidades JPA a objetos del dominio.
 * </p>
 */
@Component
public class JpaPriceRepositoryAdapter implements PriceRepository
{
    // Repositorio JPA de Spring para acceder a la entidad persistente
    private final PriceJpaRepository priceJpaRepository;
    // Componente encargado del mapeo entre entidad persistente y modelo de dominio
    private final PriceMapper priceMapper;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param priceJpaRepository repositorio JPA que accede a la base de datos.
     * @param priceMapper        componente de mapeo entre entidad y modelo de dominio.
     */
    public JpaPriceRepositoryAdapter(PriceJpaRepository priceJpaRepository, PriceMapper priceMapper)
    {
        this.priceJpaRepository = priceJpaRepository;
        this.priceMapper = priceMapper;
    }

    /**
     * Recupera el precio con mayor prioridad que aplica al producto y cadena especificados
     * en la fecha proporcionada.
     *
     * @param applicationDate fecha de aplicación para la búsqueda.
     * @param productId       identificador del producto.
     * @param brandId         identificador de la cadena.
     * @return un {@link Price} aplicable, si existe; de lo contrario, vacío.
     */
    @Override
    public Optional<Price> findTopPriceByProductIdAndBrandIdAndDate(LocalDateTime applicationDate,
                                                                    Long productId,
                                                                    Long brandId)
    {
        return priceJpaRepository
                .findTopApplicablePrice(applicationDate, productId, brandId, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .map(priceMapper::toDomain);
    }
}
