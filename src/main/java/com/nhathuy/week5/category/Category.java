package com.nhathuy.week5.category;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "Categories") // Đặt đúng tên bảng trong SQL Server
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng trong SQL Server
    @Column(name = "Id")
    private Integer id;

    @NotBlank(message = "Name is required")
    @Column(name = "Name", nullable = false, length = 150)
    private String name;

    @NotBlank(message = "Slug is required")
    @Column(name = "Slug", nullable = false, length = 160)
    private String slug;

    @Column(name = "Description", length = 500)
    private String description;

    @Column(name = "Active", nullable = false)
    private Boolean active = true;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters & setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
