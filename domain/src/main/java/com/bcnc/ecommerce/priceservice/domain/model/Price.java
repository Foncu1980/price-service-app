package com.bcnc.ecommerce.priceservice.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

// Comentario: Dependiendo de

/**
 * Modelo de dominio que representa un precio aplicable a un producto de una cadena
 * en un intervalo de fechas determinado.
 * <p>
 * Incluye información como la tarifa (priceList), prioridad de aplicación,
 * y precio final en una determinada moneda.
 * </p>
 * <p>
 * Esta clase se utiliza en la lógica de negocio y en la capa de aplicación
 * para resolver qué tarifa se aplica en una fecha concreta.
 */
public final class Price
{
    private final Long brandId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Integer priceList;
    private final Long productId;
    private final Integer priority;
    private final BigDecimal price;
    private final String curr;

    /**
     * Crea una nueva instancia inmutable del modelo de dominio {@code Price}.
     * <p>
     * Este constructor valida que todos los argumentos sean válidos antes de crear el objeto.
     * Lanza una {@link IllegalArgumentException} si alguno de los parámetros no cumple con los requisitos esperados.
     * </p>
     *
     * @param brandId   identificador de la cadena (no nulo y positivo).
     * @param startDate fecha de inicio del periodo de validez del precio (no nula).
     * @param endDate   fecha de fin del periodo de validez del precio (no nula y posterior a {@code startDate}).
     * @param priceList identificador de la tarifa (no nulo y positivo).
     * @param productId identificador del producto (no nulo y positivo).
     * @param priority  prioridad de la tarifa (no nula y no negativa).
     * @param price     valor monetario del precio (no nulo y ≥ 0).
     * @param curr      código de la moneda (no nulo ni vacío).
     * @throws IllegalArgumentException si algún argumento es nulo, negativo o inconsistente.
     */
    public Price(Long brandId, LocalDateTime startDate, LocalDateTime endDate, Integer priceList,
                 Long productId, Integer priority, BigDecimal price, String curr)
    {
        // Validar nulos primero usando Objects.requireNonNull
        this.brandId = Objects.requireNonNull(brandId, "brandId no puede ser nulo");
        this.productId = Objects.requireNonNull(productId, "productId no puede ser nulo");
        this.startDate = Objects.requireNonNull(startDate, "startDate no puede ser nulo");
        this.endDate = Objects.requireNonNull(endDate, "endDate no puede ser nulo");
        this.priceList = Objects.requireNonNull(priceList, "priceList no puede ser nulo");
        this.priority = Objects.requireNonNull(priority, "priority no puede ser nulo");
        this.price = Objects.requireNonNull(price, "price no puede ser nulo");
        this.curr = Objects.requireNonNull(curr, "curr no puede ser nulo");

        // Validar valores negativos y otras reglas de negocio
        validateNonNegativeValue(brandId, "brandId");
        validateNonNegativeValue(productId, "productId");
        validateNonNegativeValue(priceList, "priceList");
        validateNonNegativeValue(priority, "priority");
        validatePrice(price);
        validateDates(startDate, endDate);
        validateCurrency(curr);
    }

    /**
     * Valida que el valor numérico proporcionado no sea negativo.
     */
    private void validateNonNegativeValue(Number value, String fieldName)
    {
        if (value.longValue() < 0)
        {
            throw new IllegalArgumentException(fieldName + " no puede ser negativo");
        }
    }

    /**
     * Valida que el precio proporcionado sea mayor o igual que cero.
     */
    private void validatePrice(BigDecimal price)
    {
        if (price.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new IllegalArgumentException("El precio debe ser >= 0. Valor recibido: " + price);
        }
    }

    /**
     * Valida que la fecha de fin no sea anterior a la fecha de inicio.
     */
    private void validateDates(LocalDateTime startDate, LocalDateTime endDate)
    {
        if (endDate.isBefore(startDate))
        {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }

    /**
     * Valida que el código de moneda no esté vacío ni en blanco.
     */
    private void validateCurrency(String curr)
    {
        if (curr.isBlank())
        {
            throw new IllegalArgumentException("curr no puede estar vacío");
        }
    }

    // Getters
    public Long getBrandId()
    {
        return brandId;
    }

    public LocalDateTime getStartDate()
    {
        return startDate;
    }

    public LocalDateTime getEndDate()
    {
        return endDate;
    }

    public Integer getPriceList()
    {
        return priceList;
    }

    public Long getProductId()
    {
        return productId;
    }

    public Integer getPriority()
    {
        return priority;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public String getCurr()
    {
        return curr;
    }

    @Override
    public String toString()
    {
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Price otherPrice)) return false;
        return Objects.equals(brandId, otherPrice.brandId) &&
                Objects.equals(startDate, otherPrice.startDate) &&
                Objects.equals(endDate, otherPrice.endDate) &&
                Objects.equals(priceList, otherPrice.priceList) &&
                Objects.equals(productId, otherPrice.productId) &&
                Objects.equals(priority, otherPrice.priority) &&
                Objects.equals(price, otherPrice.price) &&
                Objects.equals(curr, otherPrice.curr);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(brandId, startDate, endDate, priceList, productId, priority, price, curr);
    }
}