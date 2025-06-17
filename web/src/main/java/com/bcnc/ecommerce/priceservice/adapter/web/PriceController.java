package com.bcnc.ecommerce.priceservice.adapter.web;

import com.bcnc.ecommerce.priceservice.adapter.web.dto.PriceResponse;
import com.bcnc.ecommerce.priceservice.application.PriceService;
import com.bcnc.ecommerce.priceservice.domain.model.Price;

import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Controlador REST que expone un endpoint para consultar el precio aplicable
 * a un producto de una marca en una fecha determinada.
 */
@RestController
@RequestMapping("/prices")
@Validated
public class PriceController
{
    private static final Logger LOG = LoggerFactory.getLogger(PriceController.class);

    private final PriceService priceService;

    public PriceController(PriceService priceService)
    {
        this.priceService = priceService;
    }

    /**
     * Endpoint que calcula el precio aplicable dado un producto, marca y fecha.
     *
     * @param applicationDate fecha y hora de aplicación del precio (en formato ISO).
     * @param productId ID del producto.
     * @param brandId ID de la marca.
     * @return respuesta con los datos del precio aplicable.
     */
    @GetMapping("/calculate") //
    public ResponseEntity<PriceResponse> getApplicablePrice(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam @Min(0) Long productId,
            @RequestParam @Min(0) Long brandId)
    {
        LOG.info("Recibida petición GET /calculate con applicationDate={}, productId={}, brandId={}",
                applicationDate, productId, brandId);

        Price price = priceService.findApplicablePrice(applicationDate, productId, brandId);

        LOG.info("Precio calculado devuelto: {}", price);
        return ResponseEntity.ok(mapToResponse(price));
    }

    /**
     * Mapea el objeto dominio {@link Price} a un DTO {@link PriceResponse}.
     *
     * @param price objeto dominio Price
     * @return DTO PriceResponse
     */
    private PriceResponse mapToResponse(Price price)
    {
        return new PriceResponse(
                price.getProductId(),
                price.getBrandId(),
                price.getPriceList(),
                price.getStartDate(),
                price.getEndDate(),
                price.getPrice(),
                price.getCurr()
        );
    }
}