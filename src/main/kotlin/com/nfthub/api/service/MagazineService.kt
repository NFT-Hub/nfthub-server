package com.nfthub.api.service

import com.nfthub.api.controller.NotFoundException
import com.nfthub.api.dto.MagazineCreateRequest
import com.nfthub.api.dto.MagazineResponse
import com.nfthub.api.dto.MagazineUpdateRequest
import com.nfthub.api.repository.MagazineKeywordRepository
import com.nfthub.api.repository.MagazineRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional(readOnly = true)
class MagazineService(
    private val magazineRepository: MagazineRepository,
    private val categoryService: CategoryService,
    private val keywordService: KeywordService,
    private val magazineKeywordRepository: MagazineKeywordRepository,
    private val magazineImageRepository: MagazineKeywordRepository
) {
    fun getMagazineResponse(): MagazineResponse {
        return MagazineResponse()
    }

    fun getMagazineResponses(): Page<MagazineResponse> {
        return PageImpl(listOf())
    }

    fun getMagazineOrThrow(magazineId: Long) =
        magazineRepository.findByIdOrNull(magazineId) ?: NotFoundException("not found magazineId: $magazineId")

    fun createMagazine(magazineCreateRequest: MagazineCreateRequest): MagazineResponse {
        return MagazineResponse()
    }

    fun createMagazineImage(magazineId: Long, image: MultipartFile): MagazineResponse {
        return MagazineResponse()
    }

    fun deleteMagazineImage(imageId: Long): Unit {}

    fun updateMagazineImage(imageId: Long, image: MultipartFile): MagazineResponse {
        return MagazineResponse()
    }

    fun setMagazineMainImage(imageId: Long): MagazineResponse {
        return MagazineResponse()
    }

    fun updateMagazine(magazineId: Long, request: MagazineUpdateRequest): MagazineResponse {
        return MagazineResponse()
    }

    fun updateMagazineKeywords(magazineId: Long, keywordIds: List<Long>): MagazineResponse {
        return MagazineResponse()
    }

    fun updateMagazineCategory(magazineId: Long, categoryId: Long): MagazineResponse {
        return MagazineResponse()
    }

    fun deleteMagazine(): Unit {}
}