package com.nfthub.api.controller

import com.nfthub.api.service.KeywordService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/keywords")
class KeywordController(
    private val keywordService: KeywordService
) {

    @Operation(summary = "키워드 리스트 조회")
    @GetMapping
    fun getKeywords() = keywordService.getKeywordResponses()

    @Operation(summary = "키워드 단일 조회")
    @GetMapping("/{keywordId}")
    fun getKeyword(
        @PathVariable keywordId: Long
    ) = keywordService.getKeywordOrThrow(keywordId)

    @Operation(summary = "키워드 생성", description = "어드민 전용")
    @PostMapping
    fun createKeyword(
        @RequestBody name: String
    ) = keywordService.createKeyword(name)

    @Operation(summary = "키워드 수정", description = "어드민 전용")
    @PatchMapping("/{keywordId}")
    fun updateKeyword(
        @PathVariable keywordId: Long,
        @RequestBody name: String
    ) = keywordService.updateKeywordName(keywordId, name)

    @Operation(summary = "키워드 삭제", description = "어드민 전용")
    @DeleteMapping("/{keywordId}")
    fun deleteKeyword(@PathVariable keywordId: Long) =
        keywordService.deleteKeyword(keywordId)
}