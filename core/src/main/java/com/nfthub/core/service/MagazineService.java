package com.nfthub.core.service;

import com.nfthub.core.entity.Magazine;
import com.nfthub.core.entity.MagazineImage;
import com.nfthub.core.mapper.MagazineImageMapper;
import com.nfthub.core.mapper.MagazineMapper;
import com.nfthub.core.request.MagazineRequest;
import com.nfthub.core.response.MagazineImageResponse;
import com.nfthub.core.response.MagazineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MagazineService {
    static MagazineResponse toResponse(Magazine magazine) {
        return MagazineMapper.INSTANCE.toResponse(magazine);
    }

    static MagazineImageResponse toResponse(MagazineImage magazineImage) {
        return MagazineImageMapper.INSTANCE.toResponse(magazineImage);
    }

    public MagazineResponse getMagazineResOrThrow(Long id);

    public Page<MagazineResponse> getMagazinePageRes(
            Pageable pageable,
            List<Long> tagIds,
            List<Long> categoryIds,
            List<Long> categoryGroupIds,
            String keyword, // title, tag, category, categoryGroup
            String tagKeyword,
            String categoryKeyword,
            String categoryGroupKeyword,
            String titleKeyword
    );

    public MagazineResponse createMagazine(MagazineRequest magazineRequest);

    public MagazineResponse updateMagazine(Long id, MagazineRequest magazineRequest);

    public void deleteMagazine(Long id);

    public MagazineResponse createMagazineImages(Long id, List<MultipartFile> files);

    public MagazineResponse updateMagazineImage(Long id, Long imageId, MultipartFile file);

    public void deleteMagazineImage(Long id, Long imageId);
}
