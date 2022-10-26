package com.nfthub.api.controller.manage;

import com.nfthub.core.response.TagResponse;
import com.nfthub.core.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "tag manager")
@RestController
@RequestMapping("/manage/v1/tags")
@RequiredArgsConstructor
public class ManageTagController {
    private final TagService tagService;

    @Operation(summary = "태그 생성")
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public TagResponse createTag(
            @RequestBody String name
    ) {
        return tagService.createTagOrThrow(name);
    }

    @Operation(summary = "태그 수정")
    @PatchMapping(
            value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public TagResponse updateTag(
            @PathVariable Long id,
            @RequestBody String name
    ) {
        return tagService.updateTagOrThrow(id, name);
    }

    @Operation(summary = "태그 삭제")
    @DeleteMapping("/{id}")
    public void deleteTag(
            @PathVariable Long id
    ) {
        tagService.deleteTagOrThrow(id);
    }
}
