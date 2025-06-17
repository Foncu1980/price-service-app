package com.bcnc.ecommerce.priceservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Tests de integración para PriceController")
public class PriceControllerIntegrationTest
{
    private static final String PRICE_CALCULATION_ENDPOINT = "/prices/calculate";
    private static final String PARAM_DATE = "applicationDate";
    private static final String PARAM_PRODUCT = "productId";
    private static final String PARAM_BRAND = "brandId";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Para serializar/deserializar JSON

    @Nested
    @DisplayName("Casos con precio aplicable")
    class PriceAvailableTests {

        // Test 1: petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA)
        @Test
        @DisplayName("Tarifa 1 el 14 de junio a las 10:00")
        void testGetPriceAt10AMOnDay14() throws Exception {
            mockMvc.perform(get(PRICE_CALCULATION_ENDPOINT)
                            .param(PARAM_DATE, "2020-06-14T10:00:00")
                            .param(PARAM_PRODUCT, "35455")
                            .param(PARAM_BRAND, "1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(35455))
                    .andExpect(jsonPath("$.brandId").value(1))
                    .andExpect(jsonPath("$.priceList").value(1))
                    .andExpect(jsonPath("$.price").value(35.50));
        }

        // Test 2: petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA)
        @Test
        @DisplayName("Tarifa 2 el 14 de junio a las 16:00")
        void testGetPriceAt4PMOnDay14() throws Exception {
            mockMvc.perform(get(PRICE_CALCULATION_ENDPOINT)
                            .param(PARAM_DATE, "2020-06-14T16:00:00")
                            .param(PARAM_PRODUCT, "35455")
                            .param(PARAM_BRAND, "1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(35455))
                    .andExpect(jsonPath("$.brandId").value(1))
                    .andExpect(jsonPath("$.priceList").value(2)) // Debería ser la tarifa 2 con prioridad 1
                    .andExpect(jsonPath("$.price").value(25.45));
        }

        // Test 3: petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA)
        @Test
        @DisplayName("Tarifa 1 el 14 de junio a las 21:00")
        void testGetPriceAt9PMOnDay14() throws Exception {
            mockMvc.perform(get(PRICE_CALCULATION_ENDPOINT)
                            .param(PARAM_DATE, "2020-06-14T21:00:00")
                            .param(PARAM_PRODUCT, "35455")
                            .param(PARAM_BRAND, "1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(35455))
                    .andExpect(jsonPath("$.brandId").value(1))
                    .andExpect(jsonPath("$.priceList").value(1)) // Vuelve a la tarifa 1
                    .andExpect(jsonPath("$.price").value(35.50));
        }

        // Test 4: petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA)
        @Test
        @DisplayName("Tarifa 3 el 15 de junio a las 10:00")
        void testGetPriceAt10AMOnDay15() throws Exception {
            mockMvc.perform(get(PRICE_CALCULATION_ENDPOINT)
                            .param(PARAM_DATE, "2020-06-15T10:00:00")
                            .param(PARAM_PRODUCT, "35455")
                            .param(PARAM_BRAND, "1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(35455))
                    .andExpect(jsonPath("$.brandId").value(1))
                    .andExpect(jsonPath("$.priceList").value(3)) // Tarifa 3
                    .andExpect(jsonPath("$.price").value(30.50));
        }

        // Test 5: petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA)
        @Test
        @DisplayName("Tarifa 4 el 16 de junio a las 21:00")
        void testGetPriceAt9PMOnDay16() throws Exception {
            mockMvc.perform(get(PRICE_CALCULATION_ENDPOINT)
                            .param(PARAM_DATE, "2020-06-16T21:00:00")
                            .param(PARAM_PRODUCT, "35455")
                            .param(PARAM_BRAND, "1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(35455))
                    .andExpect(jsonPath("$.brandId").value(1))
                    .andExpect(jsonPath("$.priceList").value(4)) // Tarifa 4
                    .andExpect(jsonPath("$.price").value(38.95));
        }

        @Test
        @DisplayName("Aplica tarifa 2 justo al inicio de su vigencia: 14 de junio a las 15:00")
        void testGetPriceAtStartOfTariff2() throws Exception {
            mockMvc.perform(get(PRICE_CALCULATION_ENDPOINT)
                            .param(PARAM_DATE, "2020-06-14T15:00:00")
                            .param(PARAM_PRODUCT, "35455")
                            .param(PARAM_BRAND, "1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.priceList").value(2))
                    .andExpect(jsonPath("$.price").value(25.45));
        }

        @Test
        @DisplayName("Aplica tarifa 2 justo al terminar su vigencia: 14 de junio a las 18:30")
        void testGetPriceAtEndOfTariff2() throws Exception {
            mockMvc.perform(get(PRICE_CALCULATION_ENDPOINT)
                            .param(PARAM_DATE, "2020-06-14T18:30:00")
                            .param(PARAM_PRODUCT, "35455")
                            .param(PARAM_BRAND, "1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.priceList").value(2))
                    .andExpect(jsonPath("$.price").value(25.45));
        }
    }

    @Nested
    @DisplayName("Casos sin precio aplicable")
    class PriceUnavailableTests
    {
        @Test
        @DisplayName("Devuelve 404 el 13 de junio a las 10:00")
        public void test_PriceNotFound_shouldReturn404() throws Exception {
            mockMvc.perform(get(PRICE_CALCULATION_ENDPOINT)
                            .param(PARAM_DATE, "2020-06-13T10:00:00")
                            .param(PARAM_PRODUCT, "35455")
                            .param(PARAM_BRAND, "1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Devuelve 404 para producto inexistente")
        void testProductNotFound_shouldReturn404() throws Exception {
            mockMvc.perform(get(PRICE_CALCULATION_ENDPOINT)
                            .param(PARAM_DATE, "2020-06-14T10:00:00")
                            .param(PARAM_PRODUCT, "99999") // no existe en data.sql
                            .param(PARAM_BRAND, "1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Devuelve 400 por formato de fecha inválido")
        void testInvalidDateFormat_shouldReturn400() throws Exception {
            mockMvc.perform(get(PRICE_CALCULATION_ENDPOINT) // TODO
                            .param(PARAM_DATE, "14-06-2020 10:00") // formato inválido
                            .param(PARAM_PRODUCT, "35455")
                            .param(PARAM_BRAND, "1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(jsonPath("$.message", containsString("applicationDate")))
                    .andExpect(jsonPath("$.message", containsString("Formato de parámetro inválido")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.status").value(400));
        }

        @Test
        @DisplayName("Devuelve 400 si falta parámetro requerido (productId)")
        void testMissingParameter_shouldReturn400() throws Exception {
            mockMvc.perform(get(PRICE_CALCULATION_ENDPOINT)
                            .param(PARAM_DATE, "2020-06-14T10:00:00")
                            // falta productId
                            .param(PARAM_BRAND, "1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(jsonPath("$.message", containsString("productId")))
                    .andExpect(jsonPath("$.message", containsString("Falta parámetro requerido")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.status").value(400));
        }

        @Test
        @DisplayName("Devuelve 400 para productId negativo con mensaje de error válido")
        void testNegativeProductId_shouldReturn400() throws Exception {
            mockMvc.perform(get(PRICE_CALCULATION_ENDPOINT)
                            .param(PARAM_DATE, "2020-06-14T10:00:00")
                            .param(PARAM_PRODUCT, "-1")
                            .param(PARAM_BRAND, "1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(jsonPath("$.message", containsString("productId")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.status").value(400));
        }
    }
}