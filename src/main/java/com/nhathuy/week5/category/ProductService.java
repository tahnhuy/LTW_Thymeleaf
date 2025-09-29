package com.nhathuy.week5.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
	Product save(Product product);
	Product save(Product product, MultipartFile imageFile);

	Optional<Product> findById(Integer id);
	List<Product> findAll();
	List<Product> findAll(Sort sort);
	Page<Product> findAll(Pageable pageable);
	void deleteById(Integer id);
	void delete(Product product);
	long count();
	boolean existsById(Integer id);

	List<Product> findByProductNameContaining(String name);
	Page<Product> findByProductNameContaining(String name, Pageable pageable);
	Optional<Product> findByProductName(String name);
	boolean existsByProductName(String name);

	List<Product> findByStatus(Short status);
	Page<Product> findByStatus(Short status, Pageable pageable);
	List<Product> findByUnitPriceBetween(BigDecimal min, BigDecimal max);
	Page<Product> findByUnitPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);
	List<Product> findByCategoryId(Integer categoryId);
	Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);
}


