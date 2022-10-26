package com.nfthub.core.service.impl;

import com.nfthub.core.entity.Category;
import com.nfthub.core.entity.CategoryGroup;
import com.nfthub.core.exception.AlreadyExistException;
import com.nfthub.core.exception.NotFoundException;
import com.nfthub.core.mapper.CategoryGroupMapper;
import com.nfthub.core.repository.CategoryGroupRepository;
import com.nfthub.core.response.CategoryGroupResponse;
import com.nfthub.core.service.CategoryGroupService;
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
public class CategoryGroupServiceImpl implements CategoryGroupService {
    private final CategoryGroupRepository categoryGroupRepository;
    private final CategoryService categoryService;

    @Override
    public Optional<CategoryGroup> getCategoryGroup(Long id) {
        return categoryGroupRepository.findById(id);
    }

    @Override
    public CategoryGroupResponse getCategoryGroupResOrThrow(Long id) {
        CategoryGroup categoryGroup = getCategoryGroup(id)
                .orElseThrow(() -> new NotFoundException("CategoryGroup not found"));
        return toResponse(categoryGroup);
    }

    @Override
    public List<CategoryGroupResponse> getCategoryGroupsRes() {
        List<CategoryGroup> categoryGroups = categoryGroupRepository.findAll();
        return categoryGroups.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private Optional<CategoryGroup> getCategoryGroupByName(String name) {
        return categoryGroupRepository.findByName(name);
    }

    @Override
    @Transactional
    public CategoryGroupResponse addCategoryToGroup(Long categoryGroupId, List<Long> categoryIds) {
        CategoryGroup categoryGroup = getCategoryGroup(categoryGroupId)
                .orElseThrow(() -> new NotFoundException("CategoryGroup not found"));

        categoryIds.forEach(categoryId -> {
            Category category = categoryService.getCategory(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            category.setCategoryGroup(categoryGroup);
        });

        return toResponse(categoryGroup);
    }

    @Override
    @Transactional
    public CategoryGroupResponse createCategoryGroup(String name) {
        getCategoryGroupByName(name).ifPresent(categoryGroup -> {
            throw new AlreadyExistException("CategoryGroup already exist");
        });
        CategoryGroup categoryGroup = new CategoryGroup();
        categoryGroup.setName(name);
        categoryGroupRepository.save(categoryGroup);
        return toResponse(categoryGroup);
    }

    @Override
    @Transactional
    public CategoryGroupResponse updateCategoryGroup(Long id, String name) {
        getCategoryGroupByName(name).ifPresent(categoryGroup -> {
            throw new AlreadyExistException("CategoryGroup already exist");
        });
        CategoryGroup categoryGroup = getCategoryGroup(id)
                .orElseThrow(() -> new NotFoundException("CategoryGroup not found"));
        categoryGroup.setName(name);
        return toResponse(categoryGroup);
    }

    @Override
    @Transactional
    public void deleteCategoryGroup(Long id) {
        CategoryGroup categoryGroup = getCategoryGroup(id)
                .orElseThrow(() -> new NotFoundException("CategoryGroup not found"));
        categoryGroupRepository.delete(categoryGroup);
    }

    private CategoryGroupResponse toResponse(CategoryGroup categoryGroup) {
        return CategoryGroupMapper.INSTANCE.toResponse(categoryGroup);
    }
}
