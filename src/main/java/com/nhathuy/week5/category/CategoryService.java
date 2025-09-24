package com.nhathuy.week5.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    // Legacy methods
    Page<Category> search(String keyword, Pageable pageable);
    Optional<Category> findById(Integer id);
    void deleteById(Integer id);
    
    // New API methods
    Category save(Category category);
    Category save(Category category, MultipartFile iconFile);
    
    List<Category> findAll();
    List<Category> findAll(Sort sort);
    Page<Category> findAll(Pageable pageable);
    void delete(Category category);
    long count();
    boolean existsById(Integer categoryId);
    
    // Search methods
    List<Category> findByCategoryNameContaining(String name);
    Page<Category> findByCategoryNameContaining(String name, Pageable pageable);
    Optional<Category> findByCategoryName(String name);
    boolean existsByCategoryName(String name);
}
