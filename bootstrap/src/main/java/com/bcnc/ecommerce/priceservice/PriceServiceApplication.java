package com.bcnc.ecommerce.priceservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Punto de entrada principal de la aplicación PriceService.
 * <p>
 * Esta clase arranca el contexto de Spring Boot.
 */
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
@SpringBootApplication
public class PriceServiceApplication {
    /**
     * Método principal que arranca la aplicación.
     *
     * @param args argumentos de línea de comandos (por ejemplo,
     * {@code --spring.profiles.active=dev})
     */
    public static void main(final String[] args) {
        new SpringApplicationBuilder(PriceServiceApplication.class)
            .profiles("default")
            .run(args);
    }
}
