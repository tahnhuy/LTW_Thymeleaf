package com.nhathuy.week5.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Search by product name
    List<Product> findByProductNameContaining(String name);
    List<Product> findByProductNameContainingIgnoreCase(String name);
    Page<Product> findByProductNameContaining(String name, Pageable pageable);
    Page<Product> findByProductNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Product> findByProductName(String name);
    Optional<Product> findByProductNameIgnoreCase(String name);
    boolean existsByProductName(String name);
    boolean existsByProductNameIgnoreCase(String name);

    // Search by create date
    List<Product> findByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Product> findByCreateDateAfter(LocalDateTime date);
    List<Product> findByCreateDateBefore(LocalDateTime date);

    // Search by category
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Integer categoryId);

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId")
    Page<Product> findByCategoryId(@Param("categoryId") Integer categoryId, Pageable pageable);

    List<Product> findByCategory(Category category);
    Page<Product> findByCategory(Category category, Pageable pageable);

    // Search by status
    List<Product> findByStatus(Short status);
    Page<Product> findByStatus(Short status, Pageable pageable);

    // Search by price range
    List<Product> findByUnitPriceBetween(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice);
    Page<Product> findByUnitPriceBetween(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice, Pageable pageable);

    // Count by category
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.categoryId = :categoryId")
    long countByCategoryId(@Param("categoryId") Integer categoryId);
}
