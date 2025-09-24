package com.nhathuy.week5.model;

import java.time.LocalDateTime;

public class CategoryDto {
    private Integer categoryId;
    private String categoryName;
    private String icon;
    private LocalDateTime createdAt;

    public CategoryDto() {}

    public CategoryDto(Integer categoryId, String categoryName, String icon, LocalDateTime createdAt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.icon = icon;
        this.createdAt = createdAt;
    }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}


