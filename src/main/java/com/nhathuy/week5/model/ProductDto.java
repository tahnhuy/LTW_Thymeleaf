package com.nhathuy.week5.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductDto {
    private Integer productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String images;
    private String description;
    private BigDecimal discount;
    private LocalDateTime createDate;
    private Short status;
    private CategorySummary category;

    public static class CategorySummary {
        private Integer categoryId;
        private String categoryName;

        public CategorySummary() {}
        public CategorySummary(Integer id, String name) {
            this.categoryId = id;
            this.categoryName = name;
        }
        public Integer getCategoryId() { return categoryId; }
        public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
        public String getCategoryName() { return categoryName; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    public LocalDateTime getCreateDate() { return createDate; }
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }
    public Short getStatus() { return status; }
    public void setStatus(Short status) { this.status = status; }
    public CategorySummary getCategory() { return category; }
    public void setCategory(CategorySummary category) { this.category = category; }
}


