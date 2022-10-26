package com.nfthub.api.controller;

import com.nfthub.core.response.TagResponse;
import com.nfthub.core.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tag api")
@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @Operation(summary = "태그 리스트 조회")
    @GetMapping
    @Parameters(
            value = {
                    @Parameter(
                            name = "tag",
                            description = "검색어를 통해 태그 조회",
                            schema = @Schema(type = "string")
                    )
            }
    )
    public List<TagResponse> getTagsByKeyword(
            @RequestParam(required = false) String keyword
    ) {
        return tagService.getTagsResByLikeKeyword(keyword);
    }

    @Operation(summary = "태그 단일 조회")
    @GetMapping("/{id}")
    public TagResponse getTagById(
            @PathVariable Long id
    ) {
        return tagService.getTagResByIdOrThrow(id);
    }

    @Operation(summary = "태그 이름으로 조회, 정확히 일치하는 태그 조회")
    @GetMapping("/name")
    public TagResponse getTagByName(
            @RequestParam String name
    ) {
        return tagService.getTagResByNameOrThrow(name);
    }

    @Operation(summary = "키워드로 태그 검색, like 검색 지원")
    @GetMapping("/search")
    public List<TagResponse> getTagsLikeKeyword(
            @RequestParam String keyword
    ) {
        return tagService.getTagsResByLikeKeyword(keyword);
    }
}
