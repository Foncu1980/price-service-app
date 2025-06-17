package com.bcnc.ecommerce.priceservice.application.impl;

import com.bcnc.ecommerce.priceservice.application.PriceService;
import com.bcnc.ecommerce.priceservice.application.exception.PriceNotFoundException;
import com.bcnc.ecommerce.priceservice.domain.model.Price;
// Interfaz del puerto de salida hacia la infraestructura de persistencia
import com.bcnc.ecommerce.priceservice.domain.repository.PriceRepository;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Implementación del servicio de aplicación {@link PriceService} que
 * se encarga de recuperar el precio aplicable para un producto de una marca
 * en una fecha determinada.
 *
 * <p>
 * Este servicio actúa como adaptador de entrada (caso de uso), delegando
 * la lógica de recuperación al puerto de salida {@link PriceRepository}.
 * </p>
 */
@Service
public class PriceServiceImpl implements PriceService
{
    private static final Logger LOG = LoggerFactory.getLogger(PriceServiceImpl.class);

    /** Puerto de salida: repositorio de dominio inyectado desde la infraestructura */
    private final PriceRepository priceRepository;

    /**
     * Constructor con inyección del repositorio de precios.
     *
     * @param priceRepository puerto de salida que permite acceder a precios desde la infraestructura
     */
    public PriceServiceImpl(PriceRepository priceRepository)
    {
        this.priceRepository = priceRepository;
    }

    /**
     * Recupera el precio más prioritario aplicable a un producto de una marca en una fecha dada.
     *
     * @param applicationDate Fecha de aplicación del precio
     * @param productId ID del producto
     * @param brandId ID de la marca
     * @return Precio aplicable
     * @throws PriceNotFoundException si no existe ninguna tarifa válida para los parámetros indicados
     */
    @Override
    public Price findApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId)
    {
        LOG.info("Buscando precio para productId={}, brandId={}, applicationDate={}", productId, brandId, applicationDate);
        Price price = priceRepository
                .findTopPriceByProductIdAndBrandIdAndDate(applicationDate, productId, brandId)
                .orElseThrow(() -> {
                    LOG.warn("Precio no encontrado para productId={}, brandId={}, applicationDate={}", productId, brandId, applicationDate);
                    return new PriceNotFoundException(productId, brandId, applicationDate);
                });

        LOG.info("Precio encontrado: {}", price);
        return price;
    }
}