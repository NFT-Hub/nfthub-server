package com.nfthub.api.controller.manage;

import com.nfthub.core.response.CategoryGroupResponse;
import com.nfthub.core.service.CategoryGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "category group manager")
@RestController
@RequestMapping("/manage/v1/category-groups")
@RequiredArgsConstructor
public class ManageCategoryGroupController {
    private final CategoryGroupService categoryGroupService;

    @Operation(summary = "카테고리 그룹에 카테고리 추가")
    @PostMapping("/{id}/categories")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "카테고리 그룹에 카테고리 추가 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "404", description = "카테고리 그룹 또는 카테고리가 존재하지 않음")
            }
    )
    public CategoryGroupResponse addCategoryToCategoryGroup(
            @PathVariable Long id,
            @RequestBody List<Long> categoryIds
    ) {
        return categoryGroupService.addCategoryToGroup(id, categoryIds);
    }

    @Operation(summary = "카테고리 그룹 생성")
    @PostMapping
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "카테고리 그룹 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "409", description = "이미 존재하는 카테고리 그룹")
            }
    )
    public CategoryGroupResponse createCategoryGroup(
            @RequestBody String name
    ) {
        return categoryGroupService.createCategoryGroup(name);
    }

    @Operation(summary = "카테고리 그룹 수정")
    @PatchMapping("/{id}")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "카테고리 그룹 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "404", description = "카테고리 그룹이 존재하지 않음"),
                    @ApiResponse(responseCode = "409", description = "이미 존재하는 카테고리 그룹")
            }
    )
    public CategoryGroupResponse updateCategoryGroup(
            @PathVariable Long id,
            @RequestBody String name
    ) {
        return categoryGroupService.updateCategoryGroup(id, name);
    }

    @Operation(summary = "카테고리 그룹 삭제")
    @DeleteMapping("/{id}")
    public void deleteCategoryGroup(
            @PathVariable Long id
    ) {
        categoryGroupService.deleteCategoryGroup(id);
    }
}
