package com.bcnc.ecommerce.priceservice.adapter.web.interceptor;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MetricsInterceptor implements HandlerInterceptor
{
    private final MeterRegistry meterRegistry;

    public MetricsInterceptor(MeterRegistry meterRegistry)
    {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
    {
        String uri = request.getRequestURI();

        // Ignorar rutas que no son funcionales
        if (uri.equals("/favicon.ico") ||
                uri.startsWith("/swagger-ui") ||
                uri.startsWith("/v3/api-docs"))
        {
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
