package com.bcnc.ecommerce.priceservice.adapter.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

/**
 * Clase de configuración que representa las propiedades externas
 * relacionadas con la seguridad.
 * <p>
 * Esta clase carga las propiedades definidas con el prefijo {@code security}
 * desde el fichero de configuración (por ejemplo,
 * {@code application.properties}).
 * </p>
 *
 * <p>Ejemplo:</p>
 * <pre>
 * security.token=12345678
 * </pre>
 */
@Validated
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    /**
     * Token de autenticación esperada en la cabecera HTTP Authorization.
     * Debe estar presente y no vacío.
     */
    @NotBlank
    private String token;

    /**
     * Devuelve el token de autenticación configurado.
     *
     * @return token de seguridad
     */
    public String getToken() {
        return token;
    }

    /**
     * Establece el token de autenticación que debe usarse.
     *
     * @param tokenParam valor del token a validar
     */
    public void setToken(final String tokenParam) {
        this.token = tokenParam;
    }
}
