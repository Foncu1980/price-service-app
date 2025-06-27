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

/**
 * Filtro de autenticación personalizado que intercepta todas las peticiones
 * HTTP y valida que incluyan un token Bearer válido en la cabecera
 * Authorization.
 * <p>
 * Si el token es correcto, se establece un usuario autenticado en el contexto
 * de Spring Security. Si no lo es, se responde con un estado 401 Unauthorized.
 * </p>
 *
 * <p>Las rutas públicas configuradas (como Swagger y actuator) quedan excluidas
 * de validación.</p>
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    /**
     * Logger de la clase.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    /**
     * Prefijos de rutas públicas que no requieren autenticación.
     */
    private static final String[] PUBLIC_PATHS = {
            "/actuator", "/swagger-ui", "/v3/api-docs"
    };

    /**
     * Token esperado en la cabecera Authorization.
     */
    private final String expectedToken;

    /**
     * Constructor del filtro.
     *
     * @param token el token que debe recibirse en la cabecera
     *                      Authorization
     */
    public TokenAuthenticationFilter(final String token) {
        this.expectedToken = token;
    }

    /**
     * Intercepta cada petición HTTP y aplica lógica de autenticación
     * basada en token.
     *
     * @param request      petición HTTP entrante
     * @param response     respuesta HTTP
     * @param filterChain  cadena de filtros de Spring
     * @throws ServletException en caso de error del servlet
     * @throws IOException      en caso de error de entrada/salida
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        // Rutas públicas que se deben permitir sin token
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String bearerToken = "Bearer " + expectedToken;

        if (authHeader == null
                || !authHeader.trim().equalsIgnoreCase(bearerToken)) {
            LOGGER.warn("Unauthorized access attempt to '{}' from IP {}",
                    path, request.getRemoteAddr());

            response.setHeader(HttpHeaders.WWW_AUTHENTICATE,
                    "Bearer realm=\"price-service\"");
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

        // Establece un usuario autenticado en el contexto de seguridad
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("user",
                        null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    /**
     * Verifica si la ruta es pública y puede omitirse la autenticación.
     *
     * @param path ruta del request
     * @return {@code true} si la ruta empieza por alguno de los prefijos
     * públicos
     */
    private boolean isPublicPath(final String path) {
        for (String prefix : PUBLIC_PATHS) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
