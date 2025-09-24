package com.nhathuy.week5.category;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryId")
    private Integer categoryId;

    @NotBlank(message = "Category name is required")
    @Column(name = "CategoryName", nullable = false, length = 150, unique = true)
    private String categoryName;

    @Column(name = "Icon", length = 255)
    private String icon;

    @Column(name = "CreatedAt", nullable = false, updatable = false, insertable = false,
            columnDefinition = "DATETIME2(0) DEFAULT SYSDATETIME()")
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", nullable = false, insertable = false,
            columnDefinition = "DATETIME2(0) DEFAULT SYSDATETIME()")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;

    // Constructors
    public Category() {}

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category(String categoryName, String icon) {
        this.categoryName = categoryName;
        this.icon = icon;
    }

    // Getters and Setters
    public Integer getCategoryId() { 
        return categoryId; 
    }
    
    public void setCategoryId(Integer categoryId) { 
        this.categoryId = categoryId; 
    }

    public String getCategoryName() { 
        return categoryName; 
    }
    
    public void setCategoryName(String categoryName) { 
        this.categoryName = categoryName;
    }

    public String getIcon() { 
        return icon; 
    }
    
    public void setIcon(String icon) { 
        this.icon = icon; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) { 
        this.updatedAt = updatedAt; 
    }

    public List<Product> getProducts() { 
        return products; 
    }
    
    public void setProducts(List<Product> products) { 
        this.products = products; 
    }

    // Legacy compatibility methods (for existing controller/views)
    public Integer getId() { 
        return categoryId; 
    }
    
    public void setId(Integer id) { 
        this.categoryId = id; 
    }

    public String getName() { 
        return categoryName; 
    }
    
    public void setName(String name) { 
        this.categoryName = name; 
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
