package com.nfthub.core.repository;

import com.nfthub.core.entity.MagazineTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagazineTagRepository extends JpaRepository<MagazineTag, Long> {
}
