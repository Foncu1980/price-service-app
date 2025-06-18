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

// Para esta prueba no sería necesario hacer la clase Serializable, pero aconsejable de cara a
// futuro si se usase en sesiones, caches...

/**
 * Entidad JPA que representa una tarifa de precio en la base de datos.
 *
 * <p>Está mapeada a la tabla <strong>PRICES</strong> y contiene información sobre la
 * validez, prioridad y valor de una tarifa para un producto de una cadena específica.</p>
 *
 * <p>Implementa {@link Serializable} por buenas prácticas, especialmente si se usara en
 * mecanismos como sesiones HTTP o almacenamiento en caché.</p>
 */
@Entity // cadena esta clase como una entidad JPA
@Table(name = "PRICES")
public class PriceEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Estrategia de generación de ID (auto-incremento para H2)
    @Id // cadena el campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID interno de la entidad para JPA

    @Column(name = "BRAND_ID", nullable = false)
    private Long brandId;

    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "END_DATE", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "PRICE_LIST", nullable = false)
    private Integer priceList;

    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;

    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "CURR", nullable = false)
    private String curr;

    // --- Constructores ---

    /**
     * Constructor por defecto requerido por JPA.
     */
    public PriceEntity()
    {
    }

    /**
     * Constructor completo (sin ID).
     *
     * @param brandId    ID de la cadena
     * @param startDate  Fecha de inicio de la tarifa
     * @param endDate    Fecha de fin de la tarifa
     * @param priceList  Lista de tarifas
     * @param productId  ID del producto
     * @param priority   Prioridad de la tarifa
     * @param price      Valor monetario
     * @param curr       Moneda
     */
    public PriceEntity(Long brandId, LocalDateTime startDate, LocalDateTime endDate, Integer priceList,
                       Long productId, Integer priority, BigDecimal price, String curr)
    {
        this.brandId = brandId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priceList = priceList;
        this.productId = productId;
        this.priority = priority;
        this.price = price;
        this.curr = curr;
    }

    // --- Getters ---

    public Long getId()
    {
        return id;
    }

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

    // --- Setters ---
    public void setBrandId(Long brandId)
    {
        this.brandId = brandId;
    }

    public void setStartDate(LocalDateTime startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate)
    {
        this.endDate = endDate;
    }

    public void setPriceList(Integer priceList)
    {
        this.priceList = priceList;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public void setCurr(String curr)
    {
        this.curr = curr;
    }

    @Override
    public String toString()
    {
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof PriceEntity that)) return false;

        return Objects.equals(id, that.id) &&
                Objects.equals(brandId, that.brandId) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(priceList, that.priceList) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(priority, that.priority) &&
                Objects.equals(price, that.price) &&
                Objects.equals(curr, that.curr);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, brandId, startDate, endDate, priceList, productId, priority, price, curr);
    }
}