// Category AJAX Functions
function loadCategories() {
    $.getJSON(contextPath + '/api/category', function(response) {
        if (response.status) {
            renderCategoryTable(response.body);
        } else {
            showToast('Error', response.message, 'danger');
        }
    }).fail(function() {
        showToast('Error', 'Không thể tải danh sách categories', 'danger');
    });
}

function renderCategoryTable(categories) {
    const tbody = $('#categoryTable tbody');
    tbody.empty();
    
    if (!categories || categories.length === 0) {
        tbody.append('<tr><td colspan="5" class="text-center">Không có dữ liệu</td></tr>');
        return;
    }
    
    categories.forEach(function(category) {
        const iconImg = category.icon ? 
            `<img src="${contextPath}/files/${category.icon}" alt="Icon" class="img-thumbnail">` : 
            '<i class="fas fa-image text-muted"></i>';
            
        const createdAt = category.createdAt ? 
            new Date(category.createdAt).toLocaleString('vi-VN') : 
            '';
        
        const row = `
            <tr>
                <td>${category.categoryId}</td>
                <td>${iconImg}</td>
                <td>${category.categoryName}</td>
                <td>${createdAt}</td>
                <td>
                    <div class="btn-group btn-group-sm" role="group">
                        <button type="button" class="btn btn-outline-primary edit-category-btn" 
                                data-id="${category.categoryId}" title="Edit">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button type="button" class="btn btn-outline-danger delete-category-btn" 
                                data-id="${category.categoryId}" title="Delete">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `;
        tbody.append(row);
    });
}

function saveCategoryAjax() {
    const form = $('#categoryForm')[0];
    const formData = new FormData(form);
    const categoryId = $('#categoryId').val();
    
    let url, method;
    if (categoryId) {
        // Update
        url = contextPath + '/api/category/updateCategory';
        method = 'PUT';
    } else {
        // Add new
        url = contextPath + '/api/category/addCategory';
        method = 'POST';
    }
    
    $.ajax({
        type: method,
        url: url,
        data: formData,
        contentType: false,
        processData: false,
        success: function(response) {
            if (response.status) {
                showToast('Success', response.message, 'success');
                $('#categoryModal').modal('hide');
                loadCategories();
                loadCategoryOptions(); // Refresh category options in product form
            } else {
                showToast('Error', response.message, 'danger');
            }
        },
        error: function(xhr) {
            const response = xhr.responseJSON;
            const message = response ? response.message : 'Có lỗi xảy ra khi lưu category';
            showToast('Error', message, 'danger');
        }
    });
}

function showEditCategoryModal(categoryId) {
    $.ajax({
        type: 'POST',
        url: contextPath + '/api/category/getCategory',
        data: { id: categoryId },
        success: function(response) {
            if (response.status) {
                const category = response.body;
                $('#categoryModalLabel').text('Edit Category');
                $('#categoryId').val(category.categoryId);
                $('#categoryName').val(category.categoryName);
                
                if (category.icon) {
                    $('#currentIcon').show();
                    $('#currentIconImg').attr('src', contextPath + '/files/' + category.icon);
                } else {
                    $('#currentIcon').hide();
                }
                
                $('#categoryModal').modal('show');
            } else {
                showToast('Error', response.message, 'danger');
            }
        },
        error: function() {
            showToast('Error', 'Không thể tải thông tin category', 'danger');
        }
    });
}

function deleteCategoryAjax(categoryId) {
    $.ajax({
        type: 'DELETE',
        url: contextPath + '/api/category/deleteCategory',
        data: { categoryId: categoryId },
        success: function(response) {
            if (response.status) {
                showToast('Success', response.message, 'success');
                loadCategories();
                loadCategoryOptions(); // Refresh category options
            } else {
                showToast('Error', response.message, 'danger');
            }
        },
        error: function(xhr) {
            const response = xhr.responseJSON;
            const message = response ? response.message : 'Có lỗi xảy ra khi xóa category';
            showToast('Error', message, 'danger');
        }
    });
}

function resetCategoryForm() {
    $('#categoryForm')[0].reset();
    $('#categoryModalLabel').text('Add Category');
    $('#categoryId').val('');
    $('#currentIcon').hide();
}

// Product AJAX Functions
function loadProducts() {
    $.getJSON(contextPath + '/api/product', function(response) {
        if (response.status) {
            renderProductTable(response.body);
        } else {
            showToast('Error', response.message, 'danger');
        }
    }).fail(function() {
        showToast('Error', 'Không thể tải danh sách products', 'danger');
    });
}

function renderProductTable(products) {
    const tbody = $('#productTable tbody');
    tbody.empty();
    
    if (!products || products.length === 0) {
        tbody.append('<tr><td colspan="8" class="text-center">Không có dữ liệu</td></tr>');
        return;
    }
    
    products.forEach(function(product) {
        const imageImg = product.images ? 
            `<img src="${contextPath}/files/${product.images}" alt="Product Image" class="img-thumbnail">` : 
            '<i class="fas fa-image text-muted"></i>';
            
        const categoryName = product.category ? product.category.categoryName : 'N/A';
        const statusBadge = product.status == 1 ? 
            '<span class="badge bg-success">Active</span>' : 
            '<span class="badge bg-secondary">Inactive</span>';
        
        const row = `
            <tr>
                <td>${product.productId}</td>
                <td>${imageImg}</td>
                <td>${product.productName}</td>
                <td>${categoryName}</td>
                <td>$${parseFloat(product.unitPrice).toFixed(2)}</td>
                <td>${product.quantity}</td>
                <td>${statusBadge}</td>
                <td>
                    <div class="btn-group btn-group-sm" role="group">
                        <button type="button" class="btn btn-outline-primary edit-product-btn" 
                                data-id="${product.productId}" title="Edit">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button type="button" class="btn btn-outline-danger delete-product-btn" 
                                data-id="${product.productId}" title="Delete">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `;
        tbody.append(row);
    });
}

function loadCategoryOptions() {
    $.getJSON(contextPath + '/api/category', function(response) {
        if (response.status) {
            const select = $('#productCategory');
            select.find('option:not(:first)').remove(); // Keep the first "Select Category" option
            
            response.body.forEach(function(category) {
                select.append(`<option value="${category.categoryId}">${category.categoryName}</option>`);
            });
        }
    });
}

function saveProductAjax() {
    const form = $('#productForm')[0];
    const formData = new FormData(form);
    const productId = $('#productId').val();
    
    let url, method;
    if (productId) {
        // Update
        url = contextPath + '/api/product/updateProduct';
        method = 'PUT';
    } else {
        // Add new
        url = contextPath + '/api/product/addProduct';
        method = 'POST';
    }
    
    $.ajax({
        type: method,
        url: url,
        data: formData,
        contentType: false,
        processData: false,
        success: function(response) {
            if (response.status) {
                showToast('Success', response.message, 'success');
                $('#productModal').modal('hide');
                loadProducts();
            } else {
                showToast('Error', response.message, 'danger');
            }
        },
        error: function(xhr) {
            const response = xhr.responseJSON;
            const message = response ? response.message : 'Có lỗi xảy ra khi lưu product';
            showToast('Error', message, 'danger');
        }
    });
}

function showEditProductModal(productId) {
    $.ajax({
        type: 'POST',
        url: contextPath + '/api/product/getProduct',
        data: { id: productId },
        success: function(response) {
            if (response.status) {
                const product = response.body;
                $('#productModalLabel').text('Edit Product');
                $('#productId').val(product.productId);
                $('#productName').val(product.productName);
                $('#unitPrice').val(product.unitPrice);
                $('#quantity').val(product.quantity);
                $('#discount').val(product.discount);
                $('#description').val(product.description);
                $('#status').val(product.status);
                
                if (product.category) {
                    $('#productCategory').val(product.category.categoryId);
                }
                
                if (product.images) {
                    $('#currentProductImage').show();
                    $('#currentProductImageImg').attr('src', contextPath + '/files/' + product.images);
                } else {
                    $('#currentProductImage').hide();
                }
                
                $('#productModal').modal('show');
            } else {
                showToast('Error', response.message, 'danger');
            }
        },
        error: function() {
            showToast('Error', 'Không thể tải thông tin product', 'danger');
        }
    });
}

function deleteProductAjax(productId) {
    $.ajax({
        type: 'DELETE',
        url: contextPath + '/api/product/deleteProduct',
        data: { productId: productId },
        success: function(response) {
            if (response.status) {
                showToast('Success', response.message, 'success');
                loadProducts();
            } else {
                showToast('Error', response.message, 'danger');
            }
        },
        error: function(xhr) {
            const response = xhr.responseJSON;
            const message = response ? response.message : 'Có lỗi xảy ra khi xóa product';
            showToast('Error', message, 'danger');
        }
    });
}

function resetProductForm() {
    $('#productForm')[0].reset();
    $('#productModalLabel').text('Add Product');
    $('#productId').val('');
    $('#currentProductImage').hide();
}
