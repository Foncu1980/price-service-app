package com.bcnc.ecommerce.priceservice.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Modelo de dominio que representa un precio aplicable a un producto
 * de una cadena en un intervalo de fechas determinado.
 * <p>
 * Incluye la tarifa (priceList), prioridad de aplicación, y precio
 * final en una determinada moneda.
 * </p>
 * <p>
 * Se utiliza en la lógica de negocio y en la capa de aplicación
 * para resolver qué tarifa se aplica en una fecha concreta.
 * </p>
 */
public final class Price {
    /**
     * Identificador de la cadena (brand).
     */
    private final Long brandId;

    /**
     * Fecha y hora de inicio de validez del precio.
     */
    private final LocalDateTime startDate;

    /**
     * Fecha y hora de fin de validez del precio.
     */
    private final LocalDateTime endDate;

    /**
     * Identificador de la tarifa.
     */
    private final Integer priceList;

    /**
     * Identificador del producto.
     */
    private final Long productId;

    /**
     * Prioridad del precio en caso de solapamiento.
     */
    private final Integer priority;

    /**
     * Valor monetario del precio.
     */
    private final BigDecimal price;

    /**
     * Código de la moneda (por ejemplo, "EUR").
     */
    private final String curr;

    private Price(final Builder builder) {
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
     * Builder para crear instancias de Price con validación.
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
        public Price build() {
            Objects.requireNonNull(brandId, "brandId no puede ser nulo");
            Objects.requireNonNull(productId, "productId no puede ser nulo");
            Objects.requireNonNull(startDate, "startDate no puede ser nulo");
            Objects.requireNonNull(endDate, "endDate no puede ser nulo");
            Objects.requireNonNull(priceList, "priceList no puede ser nulo");
            Objects.requireNonNull(priority, "priority no puede ser nulo");
            Objects.requireNonNull(price, "price no puede ser nulo");
            Objects.requireNonNull(curr, "curr no puede ser nulo");

            if (brandId < 0) {
                throw new IllegalArgumentException("brandId negativo");
            }
            if (productId < 0) {
                throw new IllegalArgumentException("productId negativo");
            }
            if (priceList < 0) {
                throw new IllegalArgumentException("priceList negativo");
            }
            if (priority < 0) {
                throw new IllegalArgumentException("priority negativa");
            }
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("precio < 0");
            }
            if (endDate.isBefore(startDate)) {
                throw new IllegalArgumentException(
                        "endDate antes de startDate"
                );
            }
            if (curr.isBlank()) {
                throw new IllegalArgumentException("curr vacío");
            }

            return new Price(this);
        }
    }

    /**
     * Devuelve un nuevo builder para crear instancias de {@link Price}.
     *
     * @return builder de {@link Price}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * @return identificador de cadena
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * @return fecha de inicio de validez
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * @return fecha de fin de validez
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * @return identificador de tarifa
     */
    public Integer getPriceList() {
        return priceList;
    }

    /**
     * @return identificador del producto
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @return prioridad de aplicación
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * @return valor del precio
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @return código de moneda (por ejemplo "EUR")
     */
    public String getCurr() {
        return curr;
    }

    /**
     * Representación en texto del objeto Price.
     *
     * @return representación toString
     */
    @Override
    public String toString() {
        return new StringBuilder("Price{")
                .append("brandId=").append(brandId)
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
     * Compara la igualdad de dos objetos Price.
     *
     * @param o objeto a comparar
     * @return true si son iguales
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Price other)) {
            return false;
        }
        return Objects.equals(brandId, other.brandId)
                && Objects.equals(startDate, other.startDate)
                && Objects.equals(endDate, other.endDate)
                && Objects.equals(priceList, other.priceList)
                && Objects.equals(productId, other.productId)
                && Objects.equals(priority, other.priority)
                && Objects.equals(price, other.price)
                && Objects.equals(curr, other.curr);
    }

    /**
     * Calcula el hashCode del objeto Price.
     *
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                brandId, startDate, endDate, priceList,
                productId, priority, price, curr
        );
    }

    /**
     * Verifica si este precio es aplicable en la fecha dada.
     *
     * @param date La fecha a verificar.
     * @return true si el precio es válido para la fecha, false en caso
     * contrario.
     */
    public boolean isApplicableOn(final LocalDateTime date) {

        Objects.requireNonNull(date,
                "La fecha de aplicación no puede ser nula.");
        return !date.isBefore(this.startDate) && !date.isAfter(this.endDate);
    }
}
