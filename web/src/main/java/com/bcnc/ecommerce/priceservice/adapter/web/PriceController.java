package com.bcnc.ecommerce.priceservice.adapter.web;

import com.bcnc.ecommerce.priceservice.adapter.web.dto.PriceErrorResponse;
import com.bcnc.ecommerce.priceservice.adapter.web.dto.PriceResponse;
import com.bcnc.ecommerce.priceservice.application.PriceService;
import com.bcnc.ecommerce.priceservice.domain.model.Price;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
    @Operation(summary = "Obtiene el precio aplicable a un producto para una fecha dada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Precio calculado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PriceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros de entrada inválidos o faltantes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PriceErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró tarifa aplicable",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PriceErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PriceErrorResponse.class)))
    })
    @GetMapping("/applicable") //
    public ResponseEntity<PriceResponse> getApplicablePrice(
            @Parameter(
                    name = "applicationDate",
                    in = ParameterIn.QUERY,
                    description = "Fecha y hora de aplicación en formato ISO-8601, ej: 2020-06-14T10:00:00",
                    required = true,
                    example = "2020-06-14T10:00:00",
                    schema = @Schema(type = "string", format = "date-time")
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,

            @Parameter(
                    name = "productId",
                    description = "ID del producto",
                    required = true,
                    example = "35455",
                    schema = @Schema(type = "integer", format = "int64", minimum = "0")
            )
            @RequestParam @Min(0) Long productId,

            @Parameter(
                    name = "brandId",
                    description = "ID de la marca",
                    required = true,
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64", minimum = "0")
            )
            @RequestParam @Min(0) Long brandId)
    {
        LOG.info("Recibida petición GET /applicable con applicationDate={}, productId={}, brandId={}",
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