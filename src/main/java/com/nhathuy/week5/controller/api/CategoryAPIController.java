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
import com.nhathuy.week5.category.Category;
import com.nhathuy.week5.category.CategoryService;
import com.nhathuy.week5.model.Response;

import java.util.List;
import java.util.stream.Collectors;
import com.nhathuy.week5.model.CategoryDto;
import java.util.Optional;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Category API", description = "API for managing categories")
public class CategoryAPIController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve all categories from the database")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved categories")
    public ResponseEntity<Response> getAllCategories() {
        try {
            List<CategoryDto> categories = categoryService.findAll()
                    .stream()
                    .map(c -> new CategoryDto(c.getCategoryId(), c.getCategoryName(), c.getIcon(), c.getCreatedAt()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(Response.success("Thành công", categories));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Lỗi server: " + e.getMessage()));
        }
    }

    @PostMapping("/getCategory")
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<Response> getCategory(
            @Parameter(description = "Category ID", required = true)
            @RequestParam Integer id) {
        try {
            Optional<Category> category = categoryService.findById(id);
            if (category.isPresent()) {
                return ResponseEntity.ok(Response.success("Thành công", category.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.error("Không tìm thấy category với ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Lỗi server: " + e.getMessage()));
        }
    }

    @PostMapping("/addCategory")
    @Operation(summary = "Add new category", description = "Create a new category with optional icon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Category name already exists or invalid data")
    })
    public ResponseEntity<Response> addCategory(
            @Parameter(description = "Category name", required = true)
            @RequestParam String categoryName,
            @Parameter(description = "Category icon file")
            @RequestParam(required = false) MultipartFile icon) {
        try {
            // Check if category name already exists
            if (categoryService.existsByCategoryName(categoryName)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.error("Tên category đã tồn tại: " + categoryName));
            }

            // Create new category
            Category category = new Category();
            category.setCategoryName(categoryName);

            // Save category with icon if provided
            Category savedCategory = categoryService.save(category, icon);

            return ResponseEntity.ok(Response.success("Thêm category thành công", savedCategory));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Lỗi server: " + e.getMessage()));
        }
    }

    @PutMapping("/updateCategory")
    @Operation(summary = "Update category", description = "Update an existing category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    public ResponseEntity<Response> updateCategory(
            @Parameter(description = "Category ID", required = true)
            @RequestParam Integer categoryId,
            @Parameter(description = "Category name", required = true)
            @RequestParam String categoryName,
            @Parameter(description = "Category icon file")
            @RequestParam(required = false) MultipartFile icon) {
        try {
            // Check if category exists
            Optional<Category> existingCategory = categoryService.findById(categoryId);
            if (!existingCategory.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.error("Không tìm thấy category với ID: " + categoryId));
            }

            // Check if new category name already exists (excluding current category)
            Optional<Category> categoryWithSameName = categoryService.findByCategoryName(categoryName);
            if (categoryWithSameName.isPresent() && 
                !categoryWithSameName.get().getCategoryId().equals(categoryId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.error("Tên category đã tồn tại: " + categoryName));
            }

            // Update category
            Category category = existingCategory.get();
            category.setCategoryName(categoryName);

            // Save category with new icon if provided
            Category savedCategory = categoryService.save(category, icon);

            return ResponseEntity.ok(Response.success("Cập nhật category thành công", savedCategory));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Lỗi server: " + e.getMessage()));
        }
    }

    @DeleteMapping("/deleteCategory")
    @Operation(summary = "Delete category", description = "Delete a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Category not found")
    })
    public ResponseEntity<Response> deleteCategory(
            @Parameter(description = "Category ID", required = true)
            @RequestParam Integer categoryId) {
        try {
            // Check if category exists
            if (!categoryService.existsById(categoryId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Response.error("Không tìm thấy category với ID: " + categoryId));
            }

            // Delete category
            categoryService.deleteById(categoryId);

            return ResponseEntity.ok(Response.success("Xóa category thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Lỗi server: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search categories by name", description = "Search categories by name containing the given string")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<Response> searchCategories(
            @Parameter(description = "Search term")
            @RequestParam(required = false, defaultValue = "") String name) {
        try {
            List<Category> categories;
            if (name.trim().isEmpty()) {
                categories = categoryService.findAll();
            } else {
                categories = categoryService.findByCategoryNameContaining(name);
            }
            return ResponseEntity.ok(Response.success("Tìm kiếm thành công", categories));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Lỗi server: " + e.getMessage()));
        }
    }
}
