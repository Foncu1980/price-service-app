package com.bcnc.ecommerce.priceservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Punto de entrada principal de la aplicación PriceService.
 * <p>
 * Esta clase arranca el contexto de Spring Boot.
 */
@SpringBootApplication
public class PriceServiceApplication
{
	public static void main(String[] args)
	{
		new SpringApplicationBuilder(PriceServiceApplication.class)
				.profiles("default")
				.run(args);
	}
}
