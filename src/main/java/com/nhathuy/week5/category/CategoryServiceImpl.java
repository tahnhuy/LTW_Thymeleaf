package com.nhathuy.week5.category;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.nhathuy.week5.service.IStorageService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;
    
    @Autowired
    private IStorageService storageService;

    // Legacy methods
    @Override
    public Page<Category> search(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) return repo.findAll(pageable);
        return repo.findByCategoryNameContainingIgnoreCase(keyword.trim(), pageable);
    }

    @Override
    public Optional<Category> findById(Integer id) {
        if (id == null) return Optional.empty();
        return repo.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) {
            // Before deleting, remove the icon file if it exists
            Optional<Category> category = findById(id);
            if (category.isPresent() && StringUtils.hasText(category.get().getIcon())) {
                storageService.delete(category.get().getIcon());
            }
            repo.deleteById(id);
        }
    }

    // New API methods
    @Override
    public Category save(Category category) {
        return repo.save(category);
    }

    @Override
    public Category save(Category category, MultipartFile iconFile) {
        // If category has an ID (update operation) and no new icon file is provided,
        // keep the existing icon
        if (category.getCategoryId() != null && (iconFile == null || iconFile.isEmpty())) {
            Optional<Category> existingCategory = findById(category.getCategoryId());
            if (existingCategory.isPresent()) {
                String existingIcon = existingCategory.get().getIcon();
                category.setIcon(existingIcon);
            }
        }
        // If there's a new icon file, store it
        else if (iconFile != null && !iconFile.isEmpty()) {
            String storeFilename = storageService.getStorageFilename(iconFile, 
                category.getCategoryId() != null ? category.getCategoryId().toString() : "new");
            storageService.store(iconFile, storeFilename);
            category.setIcon(storeFilename);
        }

        return save(category);
    }

    @Override
    public List<Category> findAll() {
        return repo.findAll();
    }

    @Override
    public List<Category> findAll(Sort sort) {
        return repo.findAll(sort);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public void delete(Category category) {
        // Before deleting, remove the icon file if it exists
        if (StringUtils.hasText(category.getIcon())) {
            storageService.delete(category.getIcon());
        }
        repo.delete(category);
    }

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public boolean existsById(Integer categoryId) {
        return repo.existsById(categoryId);
    }

    @Override
    public List<Category> findByCategoryNameContaining(String name) {
        return repo.findByCategoryNameContaining(name);
    }

    @Override
    public Page<Category> findByCategoryNameContaining(String name, Pageable pageable) {
        return repo.findByCategoryNameContaining(name, pageable);
    }

    @Override
    public Optional<Category> findByCategoryName(String name) {
        return repo.findByCategoryName(name);
    }

    @Override
    public boolean existsByCategoryName(String name) {
        return repo.existsByCategoryName(name);
    }
}
