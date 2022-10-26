package com.nfthub.core.service.impl;

import com.nfthub.core.entity.*;
import com.nfthub.core.exception.NotFoundException;
import com.nfthub.core.mapper.MagazineMapper;
import com.nfthub.core.repository.MagazineImageRepository;
import com.nfthub.core.repository.MagazineRepository;
import com.nfthub.core.repository.MagazineTagRepository;
import com.nfthub.core.request.MagazineRequest;
import com.nfthub.core.response.MagazineResponse;
import com.nfthub.core.service.CategoryService;
import com.nfthub.core.service.MagazineService;
import com.nfthub.core.service.S3Service;
import com.nfthub.core.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MagazineServiceImpl implements MagazineService {
    private final MagazineRepository magazineRepository;
    private final MagazineTagRepository magazineTagRepository;
    private final MagazineImageRepository magazineImageRepository;
    private final S3Service s3Service;
    private final TagService tagService;
    private final CategoryService categoryService;

    @Override
    public MagazineResponse getMagazineResOrThrow(Long id) {
        Magazine magazine = getMagazine(id).orElseThrow(
                () -> new NotFoundException("Magazine not found: requestId:" + id)
        );
        return toResponse(magazine);
    }

    private Optional<Magazine> getMagazine(Long id) {
        return magazineRepository.findById(id);
    }

    private Magazine getMagazineOrThrow(Long id) {
        return getMagazine(id).orElseThrow(
                () -> new NotFoundException("Magazine not found: requestId:" + id)
        );
    }

    @Override
    public Page<MagazineResponse> getMagazinePageRes(Pageable pageable, List<Long> tagIds, List<Long> categoryIds, List<Long> categoryGroupIds, String keyword, String tagKeyword, String categoryKeyword, String categoryGroupKeyword, String titleKeyword) {
        return null;
    }

    @Override
    @Transactional
    public MagazineResponse createMagazine(MagazineRequest magazineRequest) {
        Magazine magazine = toEntity(magazineRequest);
        setCategoryToMagazineIfExist(magazine, magazineRequest.getCategoryId());
        setTagsToMagazineIfExist(magazine, magazineRequest.getTagIds());
        magazineRepository.save(magazine);
        return toResponse(magazine);
    }

    @Override
    @Transactional
    public MagazineResponse updateMagazine(Long id, MagazineRequest request) {
        Magazine magazine = getMagazineOrThrow(id);
        setFieldsToMagazineIfExist(magazine, request);
        setCategoryToMagazineIfExist(magazine, request.getCategoryId());
        setTagsToMagazineIfExist(magazine, request.getTagIds());
        return toResponse(magazine);
    }

    private void setCategoryToMagazineIfExist(Magazine magazine, Long categoryId) {
        if (categoryId == null) {
            return;
        }
        Category category = categoryService.getCategory(categoryId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Category not found: requestId:" + categoryId);
                });
        magazine.setCategory(category);
    }

    private void setTagsToMagazineIfExist(Magazine magazine, List<Long> tagIds) {
        if (tagIds == null) {
            return;
        }
        magazine.getMagazineTags().clear();
        tagIds.forEach(tagId -> {
            Tag tag = tagService.getTagById(tagId).orElseThrow(() -> {
                throw new NotFoundException("Tag not found: requestId:" + tagId);
            });
            MagazineTag magazineTag = new MagazineTag(tag);
            magazineTagRepository.save(magazineTag);
            magazineTag.setMagazine(magazine);
        });
    }

    private void setFieldsToMagazineIfExist(Magazine magazine, MagazineRequest request) {
        if (request.getTitle() != null) {
            magazine.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            magazine.setDescription(request.getDescription());
        }
        if (request.getUrl() != null) {
            magazine.setUrl(request.getUrl());
        }
        if (request.getAuthor() != null) {
            magazine.setAuthor(request.getAuthor());
        }
    }

    @Override
    @Transactional
    public void deleteMagazine(Long id) {
        Magazine magazine = getMagazineOrThrow(id);
        magazine.getMagazineImages().forEach(magazineImage -> {
            deleteMagazineImageFromMagazine(magazine, magazineImage.getId());
        });
        magazineRepository.delete(magazine);
    }

    @Override
    @Transactional
    public MagazineResponse createMagazineImages(Long id, List<MultipartFile> files) {
        Magazine magazine = getMagazineOrThrow(id);
        files.forEach(file -> setMagazineImageToMagazine(magazine, file));
        return toResponse(magazine);
    }

    private void setMagazineImageToMagazine(Magazine magazine, MultipartFile file) {
        String imageUrl = s3Service.uploadImage(file, getMagazineImageDir(magazine.getId()));
        MagazineImage magazineImage = new MagazineImage(imageUrl, false);
        magazineImageRepository.save(magazineImage);
        magazineImage.setMagazine(magazine);
    }

    @Override
    @Transactional
    public MagazineResponse updateMagazineImage(Long id, Long imageId, MultipartFile file) {
        Magazine magazine = getMagazineOrThrow(id);
        MagazineImage magazineImage = getMagazineImageOrThrow(imageId);
        s3Service.deleteImage(magazineImage.getUrl());
        String imageUrl = s3Service.uploadImage(file, getMagazineImageDir(id));
        magazineImage.setUrl(imageUrl);
        return toResponse(magazine);
    }

    @Override
    @Transactional
    public void deleteMagazineImage(Long id, Long imageId) {
        Magazine magazine = getMagazineOrThrow(id);
        deleteMagazineImageFromMagazine(magazine, imageId);
    }

    private MagazineImage getMagazineImageOrThrow(Long id) {
        return magazineImageRepository.findById(id).orElseThrow(
                () -> new NotFoundException("MagazineImage not found: requestId:" + id)
        );
    }

    private void deleteMagazineImageFromMagazine(Magazine magazine, Long imageId) {
        MagazineImage magazineImage = getMagazineImageOrThrow(imageId);
        s3Service.deleteImage(magazineImage.getUrl());
        magazine.getMagazineImages().remove(magazineImage);
    }

    private String getMagazineImageDir(Long id) {
        return "magazineImage/" + id;
    }

    public MagazineResponse toResponse(Magazine magazine) {
        MagazineResponse response = MagazineMapper.INSTANCE.toResponse(magazine);
        response.setTags(magazine.getMagazineTags().stream().map(MagazineTag::getTag).map(TagService::toResponse).collect(Collectors.toList()));
        response.setImages(magazine.getMagazineImages().stream().map(MagazineService::toResponse).collect(Collectors.toList()));
        return response;
    }

    public Magazine toEntity(MagazineRequest magazineRequest) {
        return MagazineMapper.INSTANCE.fromEntity(magazineRequest);
    }
}
