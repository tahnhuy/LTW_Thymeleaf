package com.nhathuy.week5.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {
    Page<Category> search(String keyword, Pageable pageable);
    Category save(Category c);
    Optional<Category> findById(Integer id);
    void deleteById(Integer id);
}
