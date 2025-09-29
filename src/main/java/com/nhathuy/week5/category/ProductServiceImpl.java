package com.nhathuy.week5.category;

import com.nhathuy.week5.service.IStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository repo;
	private final IStorageService storageService;

	@Override
	public Product save(Product product) {
		return repo.save(product);
	}

	@Override
	public Product save(Product product, MultipartFile imageFile) {
		if (product.getProductId() != null && (imageFile == null || imageFile.isEmpty())) {
			return repo.save(product);
		}
		if (imageFile != null && !imageFile.isEmpty()) {
			String storeFilename = storageService.getStorageFilename(
					imageFile,
					product.getProductId() != null ? product.getProductId().toString() : "product_new"
			);
			storageService.store(imageFile, storeFilename);
			product.setImages(storeFilename);
		}
		return repo.save(product);
	}

	@Override
	public Optional<Product> findById(Integer id) {
		return repo.findById(id);
	}

	@Override
	public List<Product> findAll() {
		return repo.findAll();
	}

	@Override
	public List<Product> findAll(Sort sort) {
		return repo.findAll(sort);
	}

	@Override
	public Page<Product> findAll(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public void deleteById(Integer id) {
		repo.findById(id).ifPresent(p -> {
			if (p.getImages() != null) storageService.delete(p.getImages());
			repo.deleteById(id);
		});
	}

	@Override
	public void delete(Product product) {
		if (product.getImages() != null) storageService.delete(product.getImages());
		repo.delete(product);
	}

	@Override
	public long count() {
		return repo.count();
	}

	@Override
	public boolean existsById(Integer id) {
		return repo.existsById(id);
	}

	@Override
	public List<Product> findByProductNameContaining(String name) {
		return repo.findByProductNameContaining(name);
	}

	@Override
	public Page<Product> findByProductNameContaining(String name, Pageable pageable) {
		return repo.findByProductNameContaining(name, pageable);
	}

	@Override
	public Optional<Product> findByProductName(String name) {
		return repo.findByProductName(name);
	}

	@Override
	public boolean existsByProductName(String name) {
		return repo.existsByProductName(name);
	}

	@Override
	public List<Product> findByStatus(Short status) {
		return repo.findByStatus(status);
	}

	@Override
	public Page<Product> findByStatus(Short status, Pageable pageable) {
		return repo.findByStatus(status, pageable);
	}

	@Override
	public List<Product> findByUnitPriceBetween(BigDecimal min, BigDecimal max) {
		return repo.findByUnitPriceBetween(min, max);
	}

	@Override
	public Page<Product> findByUnitPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable) {
		return repo.findByUnitPriceBetween(min, max, pageable);
	}

	@Override
	public List<Product> findByCategoryId(Integer categoryId) {
		return repo.findByCategoryId(categoryId);
	}

	@Override
	public Page<Product> findByCategoryId(Integer categoryId, Pageable pageable) {
		return repo.findByCategoryId(categoryId, pageable);
	}
}


