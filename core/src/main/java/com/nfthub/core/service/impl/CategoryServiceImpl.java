package com.nfthub.core.service.impl;

import com.nfthub.core.entity.Category;
import com.nfthub.core.exception.AlreadyExistException;
import com.nfthub.core.exception.NotFoundException;
import com.nfthub.core.mapper.CategoryMapper;
import com.nfthub.core.repository.CategoryRepository;
import com.nfthub.core.response.CategoryResponse;
import com.nfthub.core.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> getCategoriesRes() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public Optional<Category> getCategory(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public CategoryResponse getCategoryResOrThrow(Long id) {
        Optional<Category> category = getCategory(id);
        if (category.isEmpty()) {
            throw new NotFoundException("Category not found");
        }
        return toResponse(category.get());
    }

    @Override
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public CategoryResponse getCategoryResByNameOrThrow(String name) {
        Optional<Category> category = getCategoryByName(name);
        if (category.isEmpty()) {
            throw new NotFoundException("Category not found");
        }
        return toResponse(category.get());
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(String name) {
        getCategoryByName(name).ifPresent(category -> {
            throw new AlreadyExistException("Category already exists");
        });
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        return toResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, String name) {
        getCategoryByName(name).ifPresent(category -> {
            throw new AlreadyExistException("Category already exists");
        });
        Category category = getCategory(id).orElseThrow(() -> new NotFoundException("Category not found"));
        category.setName(name);
        return toResponse(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategory(id).orElseThrow(() -> new NotFoundException("Category not found"));
        categoryRepository.delete(category);
    }

    private CategoryResponse toResponse(Category category) {
        return CategoryMapper.INSTANCE.toResponse(category);
    }
}
