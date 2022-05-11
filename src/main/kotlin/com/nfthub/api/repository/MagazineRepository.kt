package com.nfthub.api.repository

import com.nfthub.api.entity.Magazine
import com.nfthub.api.entity.MagazineImage
import com.nfthub.api.entity.MagazineTag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MagazineRepository : JpaRepository<Magazine, Long> {
}

@Repository
interface MagazineTagRepository : JpaRepository<MagazineTag, Long>

@Repository
interface MagazineImageRepository : JpaRepository<MagazineImage, Long>