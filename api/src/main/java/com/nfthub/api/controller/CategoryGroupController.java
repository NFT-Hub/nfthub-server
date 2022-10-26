package com.nfthub.api.controller;

import com.nfthub.core.response.CategoryGroupResponse;
import com.nfthub.core.service.CategoryGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "category group api", description = "카테고리 그룹 api")
@RequestMapping("/api/v1/category-groups")
@RestController
@RequiredArgsConstructor
public class CategoryGroupController {
    private final CategoryGroupService categoryGroupService;

    @Operation(summary = "카테고리 그룹 조회")
    @RequestMapping
    public List<CategoryGroupResponse> getCategoryGroups() {
        return categoryGroupService.getCategoryGroupsRes();
    }
}
