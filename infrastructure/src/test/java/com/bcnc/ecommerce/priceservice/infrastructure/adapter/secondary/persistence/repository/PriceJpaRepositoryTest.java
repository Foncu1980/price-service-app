package com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.config.JpaTestConfig;
import com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.entity.PriceEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach; // Importar BeforeEach
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase; // Importar
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace; // Importar
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

@SpringJUnitConfig
@ContextConfiguration(classes = JpaTestConfig.class)
@Transactional // para rollback automático después de cada test
@AutoConfigureTestDatabase(replace = Replace.ANY) // Asegura H2 en memoria
class PriceJpaRepositoryTest {

    @Autowired
    private PriceJpaRepository repository;

    private static final Long PRODUCT_ID = 35455L;
    private static final Long BRAND_ID = 1L;

    // Predefinir datos para mayor claridad y reutilización
    private final LocalDateTime DATE_2020_06_14_00_00_00 = LocalDateTime.of(2020, 6, 14, 0, 0, 0);
    private final LocalDateTime DATE_2020_06_14_15_00_00 = LocalDateTime.of(2020, 6, 14, 15, 0, 0);
    private final LocalDateTime DATE_2020_06_14_18_30_00 = LocalDateTime.of(2020, 6, 14, 18, 30, 0);
    private final LocalDateTime DATE_2020_06_15_00_00_00 = LocalDateTime.of(2020, 6, 15, 0, 0, 0);
    private final LocalDateTime DATE_2020_06_15_11_00_00 = LocalDateTime.of(2020, 6, 15, 11, 0, 0);
    private final LocalDateTime DATE_2020_06_15_16_00_00 = LocalDateTime.of(2020, 6, 15, 16, 0, 0);
    private final LocalDateTime DATE_2020_12_31_23_59_59 = LocalDateTime.of(2020, 12, 31, 23, 59, 59);
    private final LocalDateTime DATE_2019_01_01_00_00_00 = LocalDateTime.of(2019, 1, 1, 0, 0, 0);
    private final LocalDateTime DATE_2019_01_31_23_59_59 = LocalDateTime.of(2019, 1, 31, 23, 59, 59);

    private PriceEntity price1;
    private PriceEntity price2;
    private PriceEntity price3;
    private PriceEntity price4;
    private PriceEntity priceOffRange; // Para probar exclusión por fechas

    private PriceEntity createPriceEntity(
            final LocalDateTime start,
            final LocalDateTime end,
            final Integer priceList,
            final Integer priority,
            final BigDecimal price
    ) {
        return PriceEntity.builder()
                .brandId(BRAND_ID)
                .startDate(start)
                .endDate(end)
                .priceList(priceList)
                .productId(PRODUCT_ID)
                .priority(priority)
                .price(price)
                .curr("EUR")
                .build();
    }

    @BeforeEach // Este método se ejecuta antes de cada test.
    void setUp() {
        // La anotación @Transactional en la clase hace que la DB se limpie con rollback,
        // pero guardar los datos aquí asegura que cada test empieza con el mismo conjunto de datos.
        // No es necesario un repository.deleteAll() explícito gracias a @Transactional y create-drop en JpaTestConfig.

        // Crear las entidades de prueba (simulando los datos de tu enunciado original)
        price1 = createPriceEntity(DATE_2020_06_14_00_00_00, DATE_2020_12_31_23_59_59, 1, 0, new BigDecimal("35.50"));
        price2 = createPriceEntity(DATE_2020_06_14_15_00_00, DATE_2020_06_14_18_30_00, 2, 1, new BigDecimal("25.45"));
        price3 = createPriceEntity(DATE_2020_06_15_00_00_00, DATE_2020_06_15_11_00_00, 3, 1, new BigDecimal("30.50"));
        price4 = createPriceEntity(DATE_2020_06_15_16_00_00, DATE_2020_12_31_23_59_59, 4, 1, new BigDecimal("38.95"));
        priceOffRange = createPriceEntity(DATE_2019_01_01_00_00_00, DATE_2019_01_31_23_59_59, 5, 0, new BigDecimal("10.00"));

        repository.saveAll(List.of(price1, price2, price3, price4, priceOffRange));
    }

    @Test
    @DisplayName("Debe recuperar una sola tarifa cuando solo una es aplicable en la fecha indicada (Caso 14/06 10:00)")
    void shouldReturnBasePrice_whenOnlyBasePriceIsValid() {
        // Fecha de aplicación: 2020-06-14 10:00:00
        // Candidatos esperados: solo price1 (ya que price2 empieza a las 15:00)
        // given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        // when
        List<PriceEntity> foundPrices = repository.findApplicablePrices(applicationDate, productId, brandId,
                PageRequest.of(0, 1));

        // then
        assertAll(
                () -> assertNotNull(foundPrices),
                () -> assertEquals(1, foundPrices.size()),
                () -> assertEquals(price1, foundPrices.get(0))
        );
    }

    @Test
    @DisplayName("Precios solapados (14-06 16:00): devuelve price1 y price2")
    void shouldReturnMultiplePrices_whenOverlappingPricesExist() {
        // given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);

        // when
        List<PriceEntity> found = repository.findApplicablePrices(applicationDate, 35455L, 1L,
                Pageable.unpaged());

        // then
        assertAll(
                () -> assertEquals(2, found.size()),
                () -> assertTrue(found.contains(price1)),
                () -> assertTrue(found.contains(price2))
        );
    }

    @Test
    @DisplayName("Tarifa de mayor prioridad expirada => vuelve base price (14-06 21:00)")
    void shouldReturnBasePrice_whenHigherPriorityPriceExpired() {
        // given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 21, 0);

        // when
        List<PriceEntity> found = repository.findApplicablePrices(applicationDate, 35455L, 1L,
                PageRequest.of(0, 1));

        // then
        assertAll(
                () -> assertEquals(1, found.size()),
                () -> assertEquals(price1, found.get(0))
        );
    }

    @Test
    @DisplayName("Solapadas el 15-06 10:00 => price1 y price3")
    void shouldReturnMultiplePrices_whenTwoAreValidForDate() {
        // given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 15, 10, 0);

        // when
        List<PriceEntity> found = repository.findApplicablePrices(applicationDate, 35455L, 1L,
                Pageable.unpaged());

        // then
        assertAll(
                () -> assertEquals(2, found.size()),
                () -> assertTrue(found.contains(price1)),
                () -> assertTrue(found.contains(price3))
        );
    }

    @Test
    @DisplayName("Solapadas el 16-06 16:00 => price1 y price4")
    void shouldReturnMultiplePrices_whenDateMatchesLatePrices() {
        // given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 16, 16, 0);

        // when
        List<PriceEntity> found = repository.findApplicablePrices(applicationDate, 35455L, 1L,
                Pageable.unpaged());

        // then
        assertAll(
                () -> assertEquals(2, found.size()),
                () -> assertTrue(found.contains(price1)),
                () -> assertTrue(found.contains(price4))
        );
    }

    @Test
    @DisplayName("Fecha fuera de rango => lista vacía")
    void shouldReturnEmptyList_whenDateIsOutsideAnyPriceRange() {
        // when
        List<PriceEntity> found = repository.findApplicablePrices(
                LocalDateTime.of(2021, 1, 1, 0, 0), 35455L, 1L,
                PageRequest.of(0, 1));

        // then
        assertAll(
                () -> assertNotNull(found),
                () -> assertTrue(found.isEmpty())
        );
    }

    @Test
    @DisplayName("Producto inexistente => lista vacía")
    void shouldReturnEmptyList_whenProductDoesNotExist() {
        // when
        List<PriceEntity> found = repository.findApplicablePrices(
                LocalDateTime.of(2020, 6, 14, 16, 0), 99999L, 1L,
                PageRequest.of(0, 1));

        // then
        assertAll(
                () -> assertNotNull(found),
                () -> assertTrue(found.isEmpty())
        );
    }

    @Test
    @DisplayName("Marca inexistente => lista vacía")
    void shouldReturnEmptyList_whenBrandDoesNotExist() {
        // when
        List<PriceEntity> found = repository.findApplicablePrices(
                LocalDateTime.of(2020, 6, 14, 16, 0), 35455L, 99L,
                PageRequest.of(0, 1));

        // then
        assertAll(
                () -> assertNotNull(found),
                () -> assertTrue(found.isEmpty())
        );
    }

    @Test
    @DisplayName("Fecha igual a startDate => encuentra la tarifa")
    void shouldReturnPrices_whenDateMatchesStartDate() {
        // when
        List<PriceEntity> found = repository.findApplicablePrices(DATE_2020_06_14_00_00_00, 35455L, 1L,
                PageRequest.of(0, 1));

        // then
        assertAll(
                () -> assertEquals(1, found.size()),
                () -> assertEquals(price1, found.get(0))
        );
    }

    @Test
    @DisplayName("Fecha igual a endDate => encuentra las tarifas vigentes")
    void shouldReturnPrices_whenDateMatchesEndDate() {
        // when
        List<PriceEntity> found = repository.findApplicablePrices(DATE_2020_06_14_18_30_00, 35455L, 1L,
                Pageable.unpaged());

        // then
        assertAll(
                () -> assertEquals(2, found.size()),
                () -> assertTrue(found.contains(price1)),
                () -> assertTrue(found.contains(price2))
        );
    }

    @Test
    @DisplayName("Devuelve primero el precio con mayor prioridad cuando hay varios válidos")
    void shouldReturnHighestPriorityFirst() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0); // price1 y price2 válidos

        List<PriceEntity> found = repository.findApplicablePrices(applicationDate, 35455L, 1L, PageRequest.of(0, 2));

        assertAll(
                () -> assertEquals(2, found.size()),
                () -> assertEquals(price2, found.get(0)), // price2 tiene priority = 1, price1 = 0
                () -> assertEquals(price1, found.get(1))
        );
    }
}