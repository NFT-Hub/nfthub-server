package com.nfthub.core.service;

import com.nfthub.core.entity.CategoryGroup;
import com.nfthub.core.mapper.CategoryGroupMapper;
import com.nfthub.core.response.CategoryGroupResponse;

import java.util.List;
import java.util.Optional;

public interface CategoryGroupService {
    static CategoryGroupResponse toResponse(CategoryGroup categoryGroup) {
        return CategoryGroupMapper.INSTANCE.toResponse(categoryGroup);
    }

    Optional<CategoryGroup> getCategoryGroup(Long id);

    CategoryGroupResponse getCategoryGroupResOrThrow(Long id);

    List<CategoryGroupResponse> getCategoryGroupsRes();

    CategoryGroupResponse addCategoryToGroup(Long categoryGroupId, List<Long> categoryIds);

    CategoryGroupResponse createCategoryGroup(String name);

    CategoryGroupResponse updateCategoryGroup(Long id, String name);

    void deleteCategoryGroup(Long id);
}
