package com.nhathuy.week5.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;

    @Override
    public Page<Category> search(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) return repo.findAll(pageable);
        return repo.findByNameContainingIgnoreCase(keyword.trim(), pageable);
    }

    @Override
    public Category save(Category c) {
        // Nếu slug trống thì tự sinh từ name
        if (c.getSlug() == null || c.getSlug().isBlank()) {
            c.setSlug(toSlug(c.getName()));
        }
        return repo.save(c);
    }

    @Override
    public java.util.Optional<Category> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    // Helper: tạo slug đơn giản
    private String toSlug(String s) {
        if (s == null) return null;
        String slug = s.toLowerCase().trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
        return slug;
    }
}
