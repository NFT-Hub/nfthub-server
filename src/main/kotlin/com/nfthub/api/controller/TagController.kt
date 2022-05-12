package com.nfthub.api.controller

import com.nfthub.api.service.TagService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "tag api")
@RestController
@RequestMapping("/api/v1/tags")
class TagController(
    private val tagService: TagService
) {
    @Operation(summary = "태그 리스트 조회")
    @GetMapping
    @Parameters(
        value = [
            Parameter(
                `in` = ParameterIn.QUERY,
                description = "검색어를 통해 태그 조회",
                name = "keyword",
                schema = Schema(type = "String")
            ),
        ]
    )
    fun getTags(
        @RequestParam(required = false) keyword: String?
    ) = tagService.getTagResponses(keyword)

    @Operation(summary = "태그 단일 조회")
    @GetMapping("/{tagId}")
    fun getTag(
        @PathVariable tagId: Long
    ) = tagService.getTagOrThrow(tagId)

    @Operation(summary = "태그 이름으로 조회, 정확히 일치하는 태그 조회")
    @GetMapping("/name")
    fun getTagByName(
        @RequestParam(required = true) name: String
    ) = tagService.getTagByNameOrThrow(name)

    @Operation(summary = "키워드로 태그 검색, 유사 태그 조회")
    @GetMapping("/search")
    fun getTagsByLikeKeyword(
        @RequestParam(required = true) keyword: String
    ) = tagService.getTagsByLikeKeyword(keyword)
}

@Tag(name = "tag manager")
@RestController
@RequestMapping("/manage/v1/tags")
class ManageTagController(
    private val tagService: TagService
) {
    @Operation(summary = "태그 생성", description = "어드민 전용")
    @PostMapping
    fun createTag(
        @RequestBody name: String
    ) = tagService.createTag(name)

    @Operation(summary = "태그 수정", description = "어드민 전용")
    @PatchMapping("/{tagId}")
    fun updateTag(
        @PathVariable tagId: Long,
        @RequestBody name: String
    ) = tagService.updateTagName(tagId, name)

    @Operation(summary = "태그 삭제", description = "어드민 전용")
    @DeleteMapping("/{tagId}")
    fun deleteTag(@PathVariable tagId: Long) =
        tagService.deleteTag(tagId)
}