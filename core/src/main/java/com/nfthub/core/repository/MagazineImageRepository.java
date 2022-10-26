package com.nfthub.core.repository;

import com.nfthub.core.entity.MagazineImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagazineImageRepository extends JpaRepository<MagazineImage, Long> {
}
