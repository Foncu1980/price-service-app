package com.bcnc.ecommerce.priceservice.application.impl;

import com.bcnc.ecommerce.priceservice.application.PriceService;
import com.bcnc.ecommerce.priceservice.domain.model.Price;
// Interfaz del puerto de salida hacia la infraestructura de persistencia
import com.bcnc.ecommerce.priceservice.domain.repository.PriceRepository;

import com.bcnc.ecommerce.priceservice.domain.service.PriceSelectionService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Caso de uso de aplicación que recupera el precio aplicable para un producto
 * y cadena en una fecha determinada.
 * <p>
 * Orquesta el acceso a la infraestructura (repositorio) y la aplicación de la
 * lógica de negocio (selección por prioridad) definida en el dominio.
 * </p>
 */
@Service
public class PriceServiceImpl implements PriceService {
    /** Logger. */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(PriceServiceImpl.class);

    /** Puerto de salida: repositorio de dominio inyectado
     * desde la infraestructura. */
    private final PriceRepository priceRepository;

    /** Servicio de dominio que aplica las reglas de selección. */
    private final PriceSelectionService priceSelectionService;

    /**
     * Constructor con inyección del repositorio de precios.
     *
     * @param repository       puerto de salida que permite acceder a
     *                         precios desde la infraestructura.
     * @param selectionService servicio de dominio que aplica la lógica
     *                         de negocio de selección.
     */
    public PriceServiceImpl(final PriceRepository repository,
                            final PriceSelectionService selectionService) {
        this.priceRepository = repository;
        this.priceSelectionService = selectionService;
    }

    /**
     * Recupera el precio más prioritario aplicable a un producto y
     * cadena en una fecha dada.
     *
     * @param applicationDate fecha de aplicación del precio.
     * @param productId       ID del producto.
     * @param brandId         ID de la cadena.
     * @return precio aplicable.
     * para los parámetros indicados.
     */
    @Override
    public Price findApplicablePrice(final LocalDateTime applicationDate,
                                     final Long productId,
                                     final Long brandId) {
        LOGGER.info("Buscando precio para productId={}, brandId={}, "
                       + "applicationDate={}",
                productId, brandId, applicationDate);

        // 1. Obtener los precios candidatos desde el repositorio
        // (puerto de salida)
        List<Price> candidatePrices = priceRepository.findApplicablePrices(
                applicationDate, productId, brandId);

        // 2. Aplicar reglas del dominio para seleccionar el precio más
        // adecuado
        Price price = priceSelectionService.selectApplicablePrice(
                candidatePrices, applicationDate, productId, brandId);

        LOGGER.info("Precio encontrado: {}", price);
        return price;
    }
}
