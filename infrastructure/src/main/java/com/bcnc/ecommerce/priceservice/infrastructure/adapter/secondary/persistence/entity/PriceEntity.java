package com.bcnc.ecommerce.priceservice.infrastructure.adapter.secondary.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

// Para esta prueba no sería necesario hacer la clase Serializable,
// pero aconsejable de cara a futuro si se usase en sesiones, caches...

/**
 * Entidad JPA que representa una tarifa de precio en la base de datos.
 *
 * <p>Está mapeada a la tabla <strong>PRICES</strong> y contiene información
 * sobre la  validez, prioridad y valor de una tarifa para un
 * producto de una cadena específica.</p>
 *
 * <p>Implementa {@link Serializable} por buenas prácticas, especialmente
 * si se usara en mecanismos como sesiones HTTP o almacenamiento en caché.</p>
 */
@Entity // cadena esta clase como una entidad JPA
@Table(name = "PRICES")
public class PriceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID interno autogenerado por JPA (clave primaria).
     */
    @Id // cadena el campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID interno de la entidad para JPA

    /**
     * Identificador de la cadena (marca).
     */
    @Column(name = "BRAND_ID", nullable = false)
    private Long brandId;

    /**
     * Fecha y hora de inicio de validez de la tarifa.
     */
    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime startDate;

    /**
     * Fecha y hora de fin de validez de la tarifa.
     */
    @Column(name = "END_DATE", nullable = false)
    private LocalDateTime endDate;

    /**
     * Identificador de la lista de tarifas.
     */
    @Column(name = "PRICE_LIST", nullable = false)
    private Integer priceList;

    /**
     * Identificador del producto.
     */
    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;

    /**
     * Prioridad de aplicación de la tarifa (mayor prioridad prevalece).
     */
    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;

    /**
     * Precio monetario que aplica.
     */
    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    /**
     * Código de la moneda (por ejemplo, "EUR").
     */
    @Column(name = "CURR", nullable = false)
    private String curr;

    // --- Constructores ---

    /**
     * Constructor por defecto requerido por JPA.
     */
    public PriceEntity() {
    }

    private PriceEntity(final PriceEntity.Builder builder) {
        this.brandId = builder.brandId;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.priceList = builder.priceList;
        this.productId = builder.productId;
        this.priority = builder.priority;
        this.price = builder.price;
        this.curr = builder.curr;
    }

    /**
     * Builder para crear instancias de PriceEntity.
     */
    public static final class Builder {

        /**
         * Identificador de la cadena (brandId) para el que aplica el precio.
         */
        private Long brandId;

        /**
         * Fecha y hora desde la cual el precio es válido.
         */
        private LocalDateTime startDate;

        /**
         * Fecha y hora hasta la cual el precio es válido.
         */
        private LocalDateTime endDate;

        /**
         * Identificador de la tarifa o price list.
         */
        private Integer priceList;

        /**
         * Identificador del producto al que se aplica el precio.
         */
        private Long productId;

        /**
         * Prioridad en caso de solapamiento con otros precios.
         */
        private Integer priority;

        /**
         * Valor monetario del precio.
         */
        private BigDecimal price;

        /**
         * Código de la moneda (por ejemplo, "EUR").
         */
        private String curr;

        /**
         * Establece el identificador de cadena.
         *
         * @param inputBrandId identificador de la cadena
         * @return el builder actualizado
         */
        public Builder brandId(final Long inputBrandId) {
            this.brandId = inputBrandId;
            return this;
        }

        /**
         * Establece la fecha de inicio.
         *
         * @param inputStartDate fecha de inicio
         * @return el builder actualizado
         */
        public Builder startDate(final LocalDateTime inputStartDate) {
            this.startDate = inputStartDate;
            return this;
        }

        /**
         * Establece la fecha de fin.
         *
         * @param inputEndDate fecha de fin
         * @return el builder actualizado
         */
        public Builder endDate(final LocalDateTime inputEndDate) {
            this.endDate = inputEndDate;
            return this;
        }

        /**
         * Establece el identificador de tarifa.
         *
         * @param inputPriceList identificador de tarifa
         * @return el builder actualizado
         */
        public Builder priceList(final Integer inputPriceList) {
            this.priceList = inputPriceList;
            return this;
        }

        /**
         * Establece el identificador del producto.
         *
         * @param inputProductId identificador de producto
         * @return el builder actualizado
         */
        public Builder productId(final Long inputProductId) {
            this.productId = inputProductId;
            return this;
        }

        /**
         * Establece la prioridad.
         *
         * @param inputPriority prioridad
         * @return el builder actualizado
         */
        public Builder priority(final Integer inputPriority) {
            this.priority = inputPriority;
            return this;
        }

        /**
         * Establece el precio.
         *
         * @param inputPrice precio
         * @return el builder actualizado
         */
        public Builder price(final BigDecimal inputPrice) {
            this.price = inputPrice;
            return this;
        }

        /**
         * Establece el código de la moneda.
         *
         * @param inputCurr código de moneda
         * @return el builder actualizado
         */
        public Builder curr(final String inputCurr) {
            this.curr = inputCurr;
            return this;
        }

        /**
         * Construye una instancia inmutable de Price.
         *
         * @return instancia de Price
         */
        public PriceEntity build() {
            return new PriceEntity(this);
        }

    }

    /**
     * Devuelve un nuevo builder para crear instancias de {@link PriceEntity}.
     *
     * @return builder de {@link PriceEntity}
     */
    public static Builder builder() {
        return new Builder();
    }

    // --- Getters ---
    /**
     * Obtiene el ID interno de la entidad (clave primaria).
     *
     * @return identificador único generado por JPA.
     */
    public Long getId() {
        return id;
    }

    /**
     * Obtiene el ID de la cadena (marca).
     *
     * @return identificador de la cadena.
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * Obtiene la fecha de inicio de validez de la tarifa.
     *
     * @return fecha y hora de inicio.
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Obtiene la fecha de fin de validez de la tarifa.
     *
     * @return fecha y hora de finalización.
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * Obtiene el identificador de la tarifa.
     *
     * @return número de lista de precios.
     */
    public Integer getPriceList() {
        return priceList;
    }

    /**
     * Obtiene el ID del producto al que aplica la tarifa.
     *
     * @return identificador del producto.
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * Obtiene la prioridad de la tarifa (mayor prioridad
     * sobrescribe tarifas solapadas).
     *
     * @return prioridad de aplicación.
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * Obtiene el valor monetario de la tarifa.
     *
     * @return precio como valor decimal.
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Obtiene el código de la moneda del precio.
     *
     * @return moneda (por ejemplo, "EUR").
     */
    public String getCurr() {
        return curr;
    }

// --- Setters ---

    /**
     * Establece el ID de la cadena (marca).
     *
     * @param brandIdParam nuevo identificador de cadena.
     */
    public void setBrandId(final Long brandIdParam) {
        this.brandId = brandIdParam;
    }

    /**
     * Establece la fecha de inicio de validez de la tarifa.
     *
     * @param startDateParam nueva fecha de inicio.
     */
    public void setStartDate(final LocalDateTime startDateParam) {
        this.startDate = startDateParam;
    }

    /**
     * Establece la fecha de fin de validez de la tarifa.
     *
     * @param endDateParam nueva fecha de fin.
     */
    public void setEndDate(final LocalDateTime endDateParam) {
        this.endDate = endDateParam;
    }

    /**
     * Establece el identificador de la lista de precios.
     *
     * @param priceListParam nuevo ID de tarifa.
     */
    public void setPriceList(final Integer priceListParam) {
        this.priceList = priceListParam;
    }

    /**
     * Establece el ID del producto.
     *
     * @param productIdParam nuevo identificador del producto.
     */
    public void setProductId(final Long productIdParam) {
        this.productId = productIdParam;
    }

    /**
     * Establece la prioridad de la tarifa.
     *
     * @param priorityParam nueva prioridad.
     */
    public void setPriority(final Integer priorityParam) {
        this.priority = priorityParam;
    }

    /**
     * Establece el valor monetario del precio.
     *
     * @param priceParam nuevo valor monetario.
     */
    public void setPrice(final BigDecimal priceParam) {
        this.price = priceParam;
    }

    /**
     * Establece el código de la moneda.
     *
     * @param currParam nuevo código de moneda.
     */
    public void setCurr(final String currParam) {
        this.curr = currParam;
    }

    /**
     * Devuelve una representación en texto del objeto {@code PriceEntity},
     * que incluye todos sus campos. Útil para tareas de depuración
     * y registros (logging).
     *
     * @return una cadena que representa el estado del objeto.
     */
    @Override
    public String toString() {
        return new StringBuilder("PriceEntity{")
                .append("id=").append(id)
                .append(", brandId=").append(brandId)
                .append(", startDate=").append(startDate)
                .append(", endDate=").append(endDate)
                .append(", priceList=").append(priceList)
                .append(", productId=").append(productId)
                .append(", priority=").append(priority)
                .append(", price=").append(price)
                .append(", curr='").append(curr).append('\'')
                .append('}')
                .toString();
    }


    /**
     * Compara la igualdad de dos objetos PriceEntity.
     *
     * @param o objeto a comparar
     * @return true si son iguales
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof PriceEntity that)) {
            return false;
        }

        return Objects.equals(id, that.id)
                && Objects.equals(brandId, that.brandId)
                && Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate)
                && Objects.equals(priceList, that.priceList)
                && Objects.equals(productId, that.productId)
                && Objects.equals(priority, that.priority)
                && Objects.equals(price, that.price)
                && Objects.equals(curr, that.curr);
    }

    /**
     * Calcula el código hash de este objeto {@code PriceEntity}
     * en función de todos sus atributos.
     *
     * <p>Este método es coherente con {@link #equals(Object)} y es
     * necesario para el correcto funcionamiento en colecciones que
     * utilizan hashing, como {@link java.util.HashMap} o
     * {@link java.util.HashSet}.</p>
     *
     * @return el valor del código hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, brandId, startDate, endDate, priceList,
                productId, priority, price, curr);
    }
}
