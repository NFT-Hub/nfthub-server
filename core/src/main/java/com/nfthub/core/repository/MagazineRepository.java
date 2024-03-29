package com.nfthub.core.repository;

import com.nfthub.core.entity.Magazine;
import com.nfthub.core.repository.support.MagazineRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagazineRepository extends JpaRepository<Magazine, Long>, MagazineRepositorySupport {
}
