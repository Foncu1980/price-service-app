package com.bcnc.ecommerce.priceservice.adapter.web.interceptor;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MetricsInterceptor implements HandlerInterceptor {
    /**
     * Registro de métricas de Prometheus.
     */
    // SpotBugs EI_EXPOSE_REP2: falso positivo justificado.
    // MeterRegistry es inyectado y no se expone ni modifica.
    private final MeterRegistry meterRegistry;

    /**
     * Constructor de la clase.
     *
     * @param registry registro de métricas de Prometheus
     */
    public MetricsInterceptor(final MeterRegistry registry) {
        this.meterRegistry = registry;
    }

    /**
     * Interceptor que recopila métricas personalizadas de cada solicitud HTTP.
     * <p>
     * Si se sobrescribe {@code afterCompletion}, asegúrese de mantener
     * la llamada a {@code super.afterCompletion()} o de replicar la lógica
     * de medición de métricas para no perder funcionalidad.
     * </p>
     */
    @Override
    public void afterCompletion(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final Object handler,
                                final Exception ex) {
        String uri = request.getRequestURI();

        // Ignorar rutas que no son funcionales
        if (uri.equals("/favicon.ico")
                || uri.startsWith("/swagger-ui")
                || uri.startsWith("/v3/api-docs")) {
            return;
        }

        String method = request.getMethod();
        String status = String.valueOf(response.getStatus());

        // Contador general etiquetado
        meterRegistry.counter("http_requests_total",
                "method", method,
                "uri", uri,
                "status", status
        ).increment();

        // Contador simple sin etiquetas
        meterRegistry.counter("http_requests_global_total").increment();
    }
}
