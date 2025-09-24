package com.nhathuy.week5.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
    // API methods for CategoryName
    List<Category> findByCategoryNameContaining(String name);
    List<Category> findByCategoryNameContainingIgnoreCase(String name);
    Page<Category> findByCategoryNameContaining(String name, Pageable pageable);
    Page<Category> findByCategoryNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Category> findByCategoryName(String name);
    Optional<Category> findByCategoryNameIgnoreCase(String name);
    boolean existsByCategoryName(String name);
    boolean existsByCategoryNameIgnoreCase(String name);
    
    // Legacy methods for backward compatibility (using getName() method)
    Page<Category> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
