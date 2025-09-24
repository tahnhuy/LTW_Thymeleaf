package com.nhathuy.week5.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.nhathuy.week5.category.Product;
import com.nhathuy.week5.category.Category;
import com.nhathuy.week5.category.ProductRepository;
import com.nhathuy.week5.category.CategoryService;
import com.nhathuy.week5.service.IStorageService;
import com.nhathuy.week5.model.Response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import com.nhathuy.week5.model.ProductDto;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@Tag(name = "Product API", description = "API for managing products")
public class ProductAPIController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private IStorageService storageService;

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve all products from the database")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved products")
    public ResponseEntity<Response> getAllProducts() {
        try {
            List<ProductDto> products = productRepository.findAll()
                    .stream()
                    .map(p -> {
                        ProductDto dto = new ProductDto();
                        dto.setProductId(p.getProductId());
                        dto.setProductName(p.getProductName());
                        dto.setQuantity(p.getQuantity());
                        dto.setUnitPrice(p.getUnitPrice());
                        dto.setImages(p.getImages());
                        dto.setDescription(p.getDescription());
                        dto.setDiscount(p.getDiscount());
                        dto.setCreateDate(p.getCreateDate());
                        dto.setStatus(p.getStatus());
                        if (p.getCategory() != null) {
                            dto.setCategory(new ProductDto.CategorySummary(
                                    p.getCategory().getCategoryId(),
                                    p.getCategory().getCategoryName()
                            ));
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(Response.success("Thành công", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Lỗi server: " + e.getMessage()));
        }
    }

    @PostMapping("/getProduct")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Response> getProduct(
            @Parameter(description = "Product ID", required = true)
            @RequestParam Integer id) {
        try {
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()) {
                return ResponseEntity.ok(Response.success("Thành công", product.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.error("Không tìm thấy product với ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Lỗi server: " + e.getMessage()));
        }
    }

    @PostMapping("/addProduct")
    @Operation(summary = "Add new product", description = "Create a new product with image upload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data or product name already exists")
    })
    public ResponseEntity<Response> addProduct(
            @Parameter(description = "Product name", required = true)
            @RequestParam String productName,
            @Parameter(description = "Product image file")
            @RequestParam(required = false) MultipartFile imageFile,
            @Parameter(description = "Unit price", required = true)
            @RequestParam BigDecimal unitPrice,
            @Parameter(description = "Discount percentage")
            @RequestParam(defaultValue = "0") BigDecimal discount,
            @Parameter(description = "Product description", required = true)
            @RequestParam String description,
            @Parameter(description = "Category ID", required = true)
            @RequestParam Integer categoryId,
            @Parameter(description = "Quantity")
            @RequestParam(defaultValue = "0") Integer quantity,
            @Parameter(description = "Status")
            @RequestParam(defaultValue = "1") Short status) {
        try {
            // Check if product name already exists
            if (productRepository.existsByProductName(productName)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.error("Tên sản phẩm đã tồn tại: " + productName));
            }

            // Check if category exists
            Optional<Category> category = categoryService.findById(categoryId);
            if (!category.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.error("Không tìm thấy category với ID: " + categoryId));
            }

            // Create new product
            Product product = new Product();
            product.setProductName(productName);
            product.setUnitPrice(unitPrice);
            product.setDiscount(discount);
            product.setDescription(description);
            product.setQuantity(quantity);
            product.setStatus(status);
            product.setCategory(category.get());

            // Handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                String storeFilename = storageService.getStorageFilename(imageFile, "product_new");
                storageService.store(imageFile, storeFilename);
                product.setImages(storeFilename);
            }

            Product savedProduct = productRepository.save(product);
            return ResponseEntity.ok(Response.success("Thêm sản phẩm thành công", savedProduct));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Lỗi server: " + e.getMessage()));
        }
    }

    @PutMapping("/updateProduct")
    @Operation(summary = "Update product", description = "Update an existing product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    public ResponseEntity<Response> updateProduct(
            @Parameter(description = "Product ID", required = true)
            @RequestParam Integer productId,
            @Parameter(description = "Product name", required = true)
            @RequestParam String productName,
            @Parameter(description = "Product image file")
            @RequestParam(required = false) MultipartFile imageFile,
            @Parameter(description = "Unit price", required = true)
            @RequestParam BigDecimal unitPrice,
            @Parameter(description = "Discount percentage")
            @RequestParam BigDecimal discount,
            @Parameter(description = "Product description", required = true)
            @RequestParam String description,
            @Parameter(description = "Category ID", required = true)
            @RequestParam Integer categoryId,
            @Parameter(description = "Quantity")
            @RequestParam Integer quantity,
            @Parameter(description = "Status")
            @RequestParam Short status) {
        try {
            // Check if product exists
            Optional<Product> existingProduct = productRepository.findById(productId);
            if (!existingProduct.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.error("Không tìm thấy product với ID: " + productId));
            }

            // Check if category exists
            Optional<Category> category = categoryService.findById(categoryId);
            if (!category.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.error("Không tìm thấy category với ID: " + categoryId));
            }

            // Check if new product name already exists (excluding current product)
            Optional<Product> productWithSameName = productRepository.findByProductName(productName);
            if (productWithSameName.isPresent() && 
                !productWithSameName.get().getProductId().equals(productId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.error("Tên sản phẩm đã tồn tại: " + productName));
            }

            // Update product
            Product product = existingProduct.get();
            product.setProductName(productName);
            product.setUnitPrice(unitPrice);
            product.setDiscount(discount);
            product.setDescription(description);
            product.setQuantity(quantity);
            product.setStatus(status);
            product.setCategory(category.get());

            // Handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                // Delete old image if exists
                if (product.getImages() != null) {
                    storageService.delete(product.getImages());
                }
                String storeFilename = storageService.getStorageFilename(imageFile, productId.toString());
                storageService.store(imageFile, storeFilename);
                product.setImages(storeFilename);
            }

            Product savedProduct = productRepository.save(product);
            return ResponseEntity.ok(Response.success("Cập nhật sản phẩm thành công", savedProduct));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Lỗi server: " + e.getMessage()));
        }
    }

    @DeleteMapping("/deleteProduct")
    @Operation(summary = "Delete product", description = "Delete a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Product not found")
    })
    public ResponseEntity<Response> deleteProduct(
            @Parameter(description = "Product ID", required = true)
            @RequestParam Integer productId) {
        try {
            // Check if product exists
            Optional<Product> product = productRepository.findById(productId);
            if (!product.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.error("Không tìm thấy product với ID: " + productId));
            }

            // Delete image file if exists
            if (product.get().getImages() != null) {
                storageService.delete(product.get().getImages());
            }

            // Delete product
            productRepository.deleteById(productId);

            return ResponseEntity.ok(Response.success("Xóa sản phẩm thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Lỗi server: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search products by name", description = "Search products by name containing the given string")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<Response> searchProducts(
            @Parameter(description = "Search term")
            @RequestParam(required = false, defaultValue = "") String name) {
        try {
            List<Product> products;
            if (name.trim().isEmpty()) {
                products = productRepository.findAll();
            } else {
                products = productRepository.findByProductNameContainingIgnoreCase(name);
            }
            return ResponseEntity.ok(Response.success("Tìm kiếm thành công", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Lỗi server: " + e.getMessage()));
        }
    }
}
