package com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.mapper;

import com.bcnc.ecommerce.priceservice.domain.model.Price;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.entity.PriceEntity;
import org.springframework.stereotype.Component;

/**
 * Componente responsable de mapear entre la entidad persistente {@link PriceEntity}
 * y el modelo de dominio {@link Price}.
 *
 * <p>Este mapeador es utilizado en adaptadores de infraestructura para traducir
 * datos entre capas de persistencia y dominio.</p>
 */
@Component
public class PriceMapper
{
    /**
     * Convierte una entidad {@link PriceEntity} en un objeto del dominio {@link Price}.
     *
     * @param entity entidad JPA a convertir.
     * @return objeto del dominio equivalente.
     */
    public Price toDomain(PriceEntity entity)
    {
        return new Price(entity.getBrandId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPriceList(),
                entity.getProductId(),
                entity.getPriority(),
                entity.getPrice(),
                entity.getCurr());
    }

    /**
     * Convierte un objeto del dominio {@link Price} en una entidad {@link PriceEntity}.
     * <p>
     * Este método se incluye por si en el futuro se permite persistir precios desde el dominio.
     * </p>
     *
     * @param price objeto del dominio a convertir.
     * @return entidad JPA equivalente.
     */
    public PriceEntity toEntity(Price price)
    {
        return new PriceEntity(price.getBrandId(),
                price.getStartDate(),
                price.getEndDate(),
                price.getPriceList(),
                price.getProductId(),
                price.getPriority(),
                price.getPrice(),
                price.getCurr());
    }
}
