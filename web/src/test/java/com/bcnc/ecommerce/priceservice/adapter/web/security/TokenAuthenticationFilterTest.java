package com.bcnc.ecommerce.priceservice.adapter.web.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

class TokenAuthenticationFilterTest {

    private static final String VALID_TOKEN = "secreto123";
    private TokenAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new TokenAuthenticationFilter(VALID_TOKEN);
    }

    @AfterEach
    void cleanUp() {
        SecurityContextHolder.clearContext(); // Limpia contexto tras cada test
    }

    @Test
    @DisplayName("Permite acceso a rutas públicas sin token")
    void allowsPublicPathWithoutToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/swagger-ui/index.html");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @DisplayName("Bloquea acceso a ruta protegida sin token")
    void blocksProtectedPathWithoutToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/prices");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Unauthorized"));
    }

    @Test
    @DisplayName("Bloquea acceso con token inválido")
    void blocksInvalidToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/prices");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer invalid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Invalid or missing Bearer token"));
    }

    @Test
    @DisplayName("Permite acceso con token válido")
    void allowsAccessWithValidToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/prices");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
