package com.nfthub.api.controller;

import com.nfthub.core.response.CategoryResponse;
import com.nfthub.core.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "category api", description = "카테고리 api")
@RequestMapping("/api/v1/categories")
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "카테고리 리스트 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "카테고리 리스트 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "찾을 수 없음")
            })
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategoriesRes();
    }

    @Operation(summary = "카테고리 단일 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "카테고리 단일 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "찾을 수 없음")
            })
    public CategoryResponse getCategory(
            @PathVariable Long id
    ) {
        return categoryService.getCategoryResOrThrow(id);
    }
}
