package com.bcnc.ecommerce.priceservice.config;

import com.bcnc.ecommerce.priceservice.domain.service.PriceSelectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de beans de servicios del dominio puro.
 */
@Configuration
public class DomainServiceConfig {

    @Bean
    public PriceSelectionService priceSelectionService() {
        return new PriceSelectionService();
    }
}
