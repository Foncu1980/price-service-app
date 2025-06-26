package com.bcnc.ecommerce.priceservice.domain.service;

import com.bcnc.ecommerce.priceservice.domain.model.Price;


import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de dominio encargado de aplicar las reglas de negocio
 * para seleccionar el precio correcto por su fecha y prioridad.
 */
public class PriceSelectionService {
    /**
     * Selecciona el precio aplicable para la fecha dada (el de mayor
     * prioridad, si hay más de uno).
     *
     * @param prices lista de precios candidatos
     * @param applicationDate fecha de aplicación
     * @return precio aplicable, si existe
     */
    public Optional<Price> selectApplicablePrice(List<Price> prices, LocalDateTime applicationDate) {
        return prices.stream()
                .filter(p -> !applicationDate.isBefore(p.getStartDate()) && !applicationDate.isAfter(p.getEndDate()))
                .max(Comparator.comparingInt(Price::getPriority));
    }
}
