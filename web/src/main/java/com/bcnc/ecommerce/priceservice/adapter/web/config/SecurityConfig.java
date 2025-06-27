package com.bcnc.ecommerce.priceservice.adapter.web.config;

import com.bcnc.ecommerce.priceservice.adapter.web.security.TokenAuthenticationFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración central de Spring Security para la API de precios.
 * <p>
 * Define un esquema de autenticación basada en un <em>Bearer&nbsp;token</em>
 * simple y establece una política <strong>stateless</strong> (sin sesiones),
 * adecuada para servicios REST.
 * </p>
 * <ul>
 *   <li>Registra un {@link TokenAuthenticationFilter} personalizado.</li>
 *   <li>Desactiva CSRF (no necesario en APIs sin sesiones de navegador).</li>
 *   <li>Permite el acceso sin autenticar a rutas públicas (Swagger,
 *   Actuator).</li>
 *   <li>Requiere autenticación para cualquier otra petición.</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {
    /**
     *
     * Rutas que quedan <em>excluidas</em> de la autenticación por token.
     * Incluyen la consola Actuator y la documentación Swagger.
     */
    private static final String[] PUBLIC_PATHS = {
            "/actuator/**", "/swagger-ui/**", "/swagger-ui.html",
            "/v3/api-docs/**"
    };

    /**
     * Propiedades de seguridad externas.
     */
    private final SecurityProperties properties;

    /**
     * Crea la configuración con las propiedades de seguridad cargadas
     * desde el fichero {@code application.properties}.
     *
     * @param securityProperties propiedades que contienen el token esperado
     */
    public SecurityConfig(final SecurityProperties securityProperties) {
        this.properties = securityProperties;
    }

    /**
     * Bean que expone el filtro de autenticación por token.
     *
     * @return instancia de {@link TokenAuthenticationFilter}
     */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(properties.getToken());
    }

    /**
     * Configura la {@link SecurityFilterChain} con las reglas de seguridad
     * aplicables a todas las peticiones HTTP.
     *
     * @param http objeto {@link HttpSecurity} provisto por Spring
     * @return cadena de filtros ya construida
     * @throws Exception si la configuración de seguridad falla
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http)
            throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_PATHS).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(tokenAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
