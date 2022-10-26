package com.nfthub.core.service;

import com.nfthub.core.entity.Category;
import com.nfthub.core.mapper.CategoryMapper;
import com.nfthub.core.response.CategoryResponse;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    static CategoryResponse toResponse(Category category) {
        return CategoryMapper.INSTANCE.toResponse(category);
    }

    List<CategoryResponse> getCategoriesRes();

    Optional<Category> getCategory(Long id);

    CategoryResponse getCategoryResOrThrow(Long id);

    Optional<Category> getCategoryByName(String name);

    CategoryResponse getCategoryResByNameOrThrow(String name);

    CategoryResponse createCategory(String name);

    CategoryResponse updateCategory(Long id, String name);

    void deleteCategory(Long id);
}
