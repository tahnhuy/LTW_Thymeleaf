package com.nhathuy.week5.category;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductId")
    private Integer productId;

    @NotBlank(message = "Product name is required")
    @Column(name = "ProductName", nullable = false, length = 500, unique = true)
    private String productName;

    @Min(value = 0, message = "Quantity must be >= 0")
    @Column(name = "Quantity", nullable = false)
    private Integer quantity = 0;

    @DecimalMin(value = "0.0", message = "Unit price must be >= 0")
    @Column(name = "UnitPrice", nullable = false, precision = 18, scale = 2)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "Images", length = 200)
    private String images;

    @NotBlank(message = "Description is required")
    @Column(name = "Description", nullable = false, length = 500)
    private String description;

    @DecimalMin(value = "0.0", message = "Discount must be >= 0")
    @DecimalMax(value = "100.0", message = "Discount must be <= 100")
    @Column(name = "Discount", nullable = false, precision = 5, scale = 2,
            columnDefinition = "DECIMAL(5,2) DEFAULT 0")
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "CreateDate", nullable = false, updatable = false, insertable = false,
            columnDefinition = "DATETIME2(0) DEFAULT SYSDATETIME()")
    private LocalDateTime createDate;

    @Column(name = "Status", nullable = false,
            columnDefinition = "SMALLINT DEFAULT 1")
    private Short status = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryId", nullable = false)
    private Category category;

    // Constructors
    public Product() {}

    public Product(String productName, String description) {
        this.productName = productName;
        this.description = description;
    }

    public Product(String productName, Integer quantity, BigDecimal unitPrice, 
                   String description, Category category) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.description = description;
        this.category = category;
    }

    // Getters and Setters
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", discount=" + discount +
                ", status=" + status +
                '}';
    }
}
