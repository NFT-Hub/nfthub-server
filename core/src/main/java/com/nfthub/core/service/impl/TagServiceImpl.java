package com.nfthub.core.service.impl;

import com.nfthub.core.entity.Tag;
import com.nfthub.core.exception.AlreadyExistException;
import com.nfthub.core.exception.NotFoundException;
import com.nfthub.core.mapper.TagMapper;
import com.nfthub.core.repository.TagRepository;
import com.nfthub.core.response.TagResponse;
import com.nfthub.core.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public List<TagResponse> getTagsRes() {
        return tagRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TagResponse> getTagsResByLikeKeyword(String keyword) {
        if (keyword == null) {
            return getTagsRes();
        }
        return tagRepository.findAllByNameLike(keyword).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public TagResponse getTagResByIdOrThrow(Long id) {
        Tag tag = getTagById(id).orElseThrow(() -> new NotFoundException("Tag not found"));
        return toResponse(tag);
    }

    @Override
    public Optional<Tag> getTagById(Long id) {
        return tagRepository.findById(id);
    }

    @Override
    public TagResponse getTagResByNameOrThrow(String name) {
        Tag tag = getTagByName(name).orElseThrow(() -> new NotFoundException("Not found tagName: " + name));
        return toResponse(tag);
    }

    public Optional<Tag> getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    @Transactional
    public TagResponse createTagOrThrow(String name) {
        getTagByName(name).ifPresent(tag -> {
            throw new AlreadyExistException("Already exist tagName: " + name);
        });
        Tag tag = new Tag();
        tag.setName(name);
        tagRepository.save(tag);
        return toResponse(tag);
    }

    @Override
    @Transactional
    public TagResponse updateTagOrThrow(Long id, String name) {
        getTagById(id).ifPresent(tag -> {
            throw new AlreadyExistException("Already exist tagName: " + name);
        });
        Tag tag = getTagById(id).orElseThrow(() -> new NotFoundException("Not found tagId: " + id));
        tag.setName(name);
        return toResponse(tag);
    }

    @Override
    @Transactional
    public void deleteTagOrThrow(Long id) {
        Tag tag = getTagById(id).orElseThrow(() -> new NotFoundException("Not found tagId: " + id));
        tagRepository.delete(tag);
    }

    public TagResponse toResponse(Tag tag) {
        return TagMapper.INSTANCE.toResponse(tag);
    }
}
