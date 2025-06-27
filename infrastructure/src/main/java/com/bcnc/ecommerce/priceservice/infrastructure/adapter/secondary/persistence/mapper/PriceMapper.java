package com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.mapper;

import com.bcnc.ecommerce.priceservice.domain.model.Price;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.entity.PriceEntity;
import org.springframework.stereotype.Component;

/**
 * Componente responsable de mapear entre la entidad persistente
 * {@link PriceEntity} y el modelo de dominio {@link Price}.
 *
 * <p>Este mapeador es utilizado en adaptadores de infraestructura para
 * traducir datos entre capas de persistencia y dominio.</p>
 */
@Component
public class PriceMapper {
    /**
     * Convierte una entidad {@link PriceEntity} en un objeto del dominio
     * {@link Price}.
     *
     * <p>Este método realiza una conversión directa, sin aplicar lógica
     * de negocio.</p>
     *
     * @param entity entidad JPA a convertir.
     * @return objeto del dominio equivalente.
     */
    public Price toDomain(final PriceEntity entity) {
        return new Price.Builder()
                .brandId(entity.getBrandId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .priceList(entity.getPriceList())
                .productId(entity.getProductId())
                .priority(entity.getPriority())
                .price(entity.getPrice())
                .curr(entity.getCurr())
                .build();
    }

    /**
     * Convierte un objeto del dominio {@link Price} en una entidad
     * {@link PriceEntity}.
     * <p>
     * Este método se incluye por si en el futuro se permite persistir precios
     * desde el dominio.
     * </p>
     *
     * @param price objeto del dominio a convertir.
     * @return entidad JPA equivalente.
     */
    public PriceEntity toEntity(final Price price) {
        return PriceEntity.builder()
                .brandId(price.getBrandId())
                .startDate(price.getStartDate())
                .endDate(price.getEndDate())
                .priceList(price.getPriceList())
                .productId(price.getProductId())
                .priority(price.getPriority())
                .price(price.getPrice())
                .curr(price.getCurr())
                .build();
    }
}
