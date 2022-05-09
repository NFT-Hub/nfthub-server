package com.nfthub.api.controller

import com.nfthub.api.dto.CategoryGroupResponse
import com.nfthub.api.dto.CategoryResponse
import com.nfthub.api.service.CategoryGroupService
import com.nfthub.api.service.CategoryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "category api", description = "카테고리 api")
@RequestMapping("/api/v1/categories")
@RestController
class CategoryController(
    private val categoryService: CategoryService
) {
    @Operation(summary = "카테고리 리스트 조회 [미구현]")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상응답"),
        ApiResponse(responseCode = "404", description = "찾을 수 없음"),
    )
    @GetMapping
    fun getCategoryResponses(): List<CategoryResponse> =
        categoryService.getCategoryResponses()

    @Operation(summary = "카테고리 단일 조회 [미구현]")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상응답"),
        ApiResponse(responseCode = "404", description = "찾을 수 없음"),
    )
    @GetMapping("/{categoryId}")
    fun getCategoryResponse(
        @PathVariable categoryId: Long
    ) = categoryService.getCategoryResponse(categoryId)

    @Operation(summary = "[어드민] 카테고리 생성 [미구현]", description = "어드민 전용")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상응답"),
        ApiResponse(responseCode = "401", description = "인증 필요"),
        ApiResponse(responseCode = "403", description = "권한 없음"),
        ApiResponse(responseCode = "409", description = "이미 존재"),
    )
    @PostMapping
    fun createCategory(
        @RequestBody name: String
    ) = categoryService.createCategory(name)

    @Operation(summary = "[어드민] 카테고리 수정 [미구현]", description = "어드민 전용")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상응답"),
        ApiResponse(responseCode = "401", description = "인증 필요"),
        ApiResponse(responseCode = "403", description = "권한 없음"),
        ApiResponse(responseCode = "404", description = "찾을 수 없음"),
        ApiResponse(responseCode = "409", description = "이미 존재"),
    )
    @PatchMapping("/{categoryId}")
    fun updateCategory(
        @RequestBody name: String, @PathVariable categoryId: Long
    ) = categoryService.updateCategoryName(categoryId, name)

    @Operation(summary = "[어드민] 카테고리 제거 [미구현]", description = "어드민 전용")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "정상응답"),
        ApiResponse(responseCode = "401", description = "인증 필요"),
        ApiResponse(responseCode = "403", description = "권한 없음"),
        ApiResponse(responseCode = "404", description = "찾을 수 없음"),
    )
    @DeleteMapping("/{categoryId}")
    fun deleteCategory(
        @PathVariable categoryId: Long
    ) = categoryService.deleteCategory(categoryId)
}

@Tag(name = "category group api", description = "카테고리 그룹 api")
@RequestMapping("/api/v1/category-groups")
@RestController
class CategoryGroupController(
    private val categoryGroupService: CategoryGroupService
) {
    @Operation(summary = "카테고리 그룹 이름 및 속해있는 카테고리 조회 [미구현]")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상응답"),
        ApiResponse(responseCode = "404", description = "찾을 수 없음"),
    )
    @GetMapping
    fun getCategoryGroupResponses(): List<CategoryGroupResponse> =
        categoryGroupService.getCategoryGroupResponses()

    @Operation(summary = "[어드민] 카테고리 그룹 이름 생성 [미구현]", description = "어드민 전용")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상응답"),
        ApiResponse(responseCode = "401", description = "인증 필요"),
        ApiResponse(responseCode = "403", description = "권한 없음"),
        ApiResponse(responseCode = "409", description = "이미 존재"),
    )
    @PostMapping
    fun createCategoryGroup(
        @RequestBody name: String
    ) = categoryGroupService.createCategoryGroup(name)

    @Operation(summary = "[어드민] 카테고리 그룹 이름 수정 [미구현]", description = "어드민 전용")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상응답"),
        ApiResponse(responseCode = "401", description = "인증 필요"),
        ApiResponse(responseCode = "403", description = "권한 없음"),
        ApiResponse(responseCode = "404", description = "찾을 수 없음"),
        ApiResponse(responseCode = "409", description = "이미 존재"),
    )
    @PatchMapping("/{categoryGroupId}")
    fun updateCategoryGroup(
        @RequestBody name: String, @PathVariable categoryGroupId: Long
    ) = categoryGroupService.updateCategoryGroupName(categoryGroupId, name)

    @Operation(summary = "[어드민] 카테고리 그룹에 카테고리 할당 [미구현]", description = "어드민 전용")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상응답"),
        ApiResponse(responseCode = "401", description = "인증 필요"),
        ApiResponse(responseCode = "403", description = "권한 없음"),
        ApiResponse(responseCode = "404", description = "찾을 수 없음"),
    )
    @PutMapping("/{categoryGroupId}/categories")
    fun addCategoryToCategoryGroup(
        @PathVariable categoryGroupId: Long,
        @RequestBody categoryIds: List<Long>
    ) = categoryGroupService.addCategoryToCategoryGroup(categoryGroupId, categoryIds)

    @Operation(summary = "[어드민] 카테고리 그룹 이름 수정 [미구현]", description = "어드민 전용")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "정상응답"),
        ApiResponse(responseCode = "401", description = "인증 필요"),
        ApiResponse(responseCode = "403", description = "권한 없음"),
        ApiResponse(responseCode = "404", description = "찾을 수 없음"),
    )
    @DeleteMapping("/{categoryGroupId}")
    fun deleteCategoryGroup(
        @PathVariable categoryGroupId: Long
    ) = categoryGroupService.deleteCategoryGroup(categoryGroupId)
}