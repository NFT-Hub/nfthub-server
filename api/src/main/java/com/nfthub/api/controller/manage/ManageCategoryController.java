package com.nfthub.api.controller.manage;

import com.nfthub.core.response.CategoryResponse;
import com.nfthub.core.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "카테고리 api")
@RequestMapping("/manage/v1/categories")
@RestController
@RequiredArgsConstructor
public class ManageCategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "카테고리 생성")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "카테고리 리스트 조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 필요"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 존재하는 값")
            })
    @PostMapping
    public List<CategoryResponse> createCategory() {
        return categoryService.getCategoriesRes();
    }

    @Operation(summary = "카테고리 수정")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "카테고리 단일 조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 필요"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
                    @ApiResponse(responseCode = "404", description = "찾을 수 없음")
            })
    @PatchMapping("/{id}")
    public CategoryResponse updateCategory(
            @PathVariable Long id,
            @RequestBody String name
    ) {
        return categoryService.updateCategory(id, name);
    }

    @Operation(summary = "카테고리 삭제")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "카테고리 단일 조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 필요"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
                    @ApiResponse(responseCode = "404", description = "찾을 수 없음")
            })
    @DeleteMapping("/{id}")
    public void deleteCategory(
            @PathVariable Long id
    ) {
        categoryService.deleteCategory(id);
    }
}
