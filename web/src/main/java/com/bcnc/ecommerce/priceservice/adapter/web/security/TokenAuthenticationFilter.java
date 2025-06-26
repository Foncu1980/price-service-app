package com.bcnc.ecommerce.priceservice.adapter.web.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class TokenAuthenticationFilter extends OncePerRequestFilter
{
    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private static final String[] PUBLIC_PATHS = {
            "/actuator", "/swagger-ui", "/v3/api-docs"
    };

    private final String expectedToken;

    public TokenAuthenticationFilter(String expectedToken)
    {
        this.expectedToken = expectedToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException
    {
        String path = request.getRequestURI();

        // Rutas públicas que se deben permitir sin token
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String bearerToken = "Bearer " + expectedToken;
        if (authHeader == null || !authHeader.trim().equalsIgnoreCase(bearerToken)) {
            logger.warn("Unauthorized access attempt to '{}' from IP {}", path, request.getRemoteAddr());

            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"price-service\"");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("""
            {
                "error": "Unauthorized",
                "message": "Invalid or missing Bearer token"
            }
            """);
            response.getWriter().close();
            return;
        }

        // ✅ Establece un usuario autenticado en el contexto de seguridad
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("user", null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        for (String prefix : PUBLIC_PATHS) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
