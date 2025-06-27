package com.bcnc.ecommerce.priceservice.adapter.web.config;

import com.bcnc.ecommerce.priceservice.adapter.web.interceptor.MetricsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de la capa web de Spring MVC.
 * <p>
 * Esta clase registra interceptores HTTP personalizados, como el
 * interceptor de métricas,
 * para que se apliquen a todas las peticiones entrantes gestionadas por
 * Spring.
 * </p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * Interceptor para recopilar métricas de las peticiones HTTP.
     */
    private final MetricsInterceptor metricsInterceptor;

    /**
     * Constructor que inyecta el interceptor de métricas.
     *
     * @param interceptor el interceptor que recopila métricas
     *                           de cada petición
     */
    @Autowired
    public WebConfig(final MetricsInterceptor interceptor) {
        this.metricsInterceptor = interceptor;
    }

    /**
     * Registra los interceptores personalizados en la aplicación.
     *
     * @param registry el registro de interceptores de Spring MVC
     */
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(metricsInterceptor);
    }
}
