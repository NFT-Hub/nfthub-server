package com.nfthub.core.service;

import com.nfthub.core.entity.Tag;
import com.nfthub.core.mapper.TagMapper;
import com.nfthub.core.response.TagResponse;

import java.util.List;
import java.util.Optional;

public interface TagService {
    static TagResponse toResponse(Tag tag) {
        return TagMapper.INSTANCE.toResponse(tag);
    }

    List<TagResponse> getTagsRes();

    List<TagResponse> getTagsResByLikeKeyword(String keyword);

    TagResponse getTagResByIdOrThrow(Long id);

    TagResponse getTagResByNameOrThrow(String name);

    Optional<Tag> getTagById(Long id);

    TagResponse createTagOrThrow(String name);

    TagResponse updateTagOrThrow(Long id, String name);

    void deleteTagOrThrow(Long id);
}
