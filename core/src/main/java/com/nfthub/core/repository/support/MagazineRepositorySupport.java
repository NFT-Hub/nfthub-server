package com.nfthub.core.repository.support;

import com.nfthub.core.entity.Magazine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MagazineRepositorySupport {
    Page<Magazine> findAllBy(
            Pageable pageable,
            List<Long> tagIds,
            List<Long> categoryIds,
            List<Long> categoryGroupIds,
            String keyword,
            String tagKeyword,
            String categoryKeyword,
            String categoryGroupKeyword,
            String titleKeyword
    );
}