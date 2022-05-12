package com.nfthub.api.controller

import com.nfthub.api.service.TagService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "tag api")
@RestController
@RequestMapping("/api/v1/tags")
class TagController(
    private val tagService: TagService
) {
    @Operation(summary = "키워드 리스트 조회")
    @GetMapping
    fun getTags() = tagService.getTagResponses()

    @Operation(summary = "키워드 단일 조회")
    @GetMapping("/{tagId}")
    fun getTag(
        @PathVariable tagId: Long
    ) = tagService.getTagOrThrow(tagId)
}

@Tag(name = "tag manager")
@RestController
@RequestMapping("/manage/v1/tags")
class ManageTagController(
    private val tagService: TagService
) {
    @Operation(summary = "키워드 생성", description = "어드민 전용")
    @PostMapping
    fun createTag(
        @RequestBody name: String
    ) = tagService.createTag(name)

    @Operation(summary = "키워드 수정", description = "어드민 전용")
    @PatchMapping("/{tagId}")
    fun updateTag(
        @PathVariable tagId: Long,
        @RequestBody name: String
    ) = tagService.updateTagName(tagId, name)

    @Operation(summary = "키워드 삭제", description = "어드민 전용")
    @DeleteMapping("/{tagId}")
    fun deleteTag(@PathVariable tagId: Long) =
        tagService.deleteTag(tagId)
}