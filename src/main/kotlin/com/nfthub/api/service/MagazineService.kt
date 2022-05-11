package com.nfthub.api.service

import com.nfthub.api.controller.NotFoundException
import com.nfthub.api.dto.*
import com.nfthub.api.entity.MagazineImage
import com.nfthub.api.entity.MagazineTag
import com.nfthub.api.repository.MagazineImageRepository
import com.nfthub.api.repository.MagazineRepository
import com.nfthub.api.repository.MagazineTagRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional(readOnly = true)
class MagazineService(
    private val magazineRepository: MagazineRepository,
    private val categoryService: CategoryService,
    private val tagService: TagService,
    private val magazineTagRepository: MagazineTagRepository,
    private val magazineImageRepository: MagazineImageRepository,
    private val s3Service: S3Service
) {

    fun getMagazineResponse(magazineId: Long): MagazineResponse = getMagazineOrThrow(magazineId).toResponse()

    fun getMagazineResponses(
        pageable: Pageable, tagIds: List<Long>?, categoryIds: List<Long>?, searchTag: String?
    ): Page<MagazineResponse> {
        return PageImpl(listOf())
    }

    fun getMagazineOrThrow(magazineId: Long) =
        magazineRepository.findByIdOrNull(magazineId) ?: throw NotFoundException("not found magazineId: $magazineId")

    @Transactional
    fun createMagazine(magazineCreateRequest: MagazineCreateRequest): MagazineResponse =
        magazineRepository.save(
            magazineCreateRequest.toEntity().apply {
                magazineCreateRequest.categoryId?.let {
                    category = categoryService.getCategoryOrThrow(it)
                }
            }
        ).apply {
            magazineCreateRequest.tagIds?.let { tagIds ->
                val magazineTags = magazineTagRepository.saveAll(
                    tagIds.map { MagazineTag(magazine = this, tag = tagService.getTagOrThrow(it)) }
                )
                this.magazineTags = magazineTags
            }
        }.toResponse()

    fun getMagazineImageOrThrow(magazineImageId: Long) =
        magazineImageRepository.findByIdOrNull(magazineImageId)
            ?: throw NotFoundException("magazineImage not exist: $magazineImageId")

    @Transactional
    fun createMagazineImage(magazineId: Long, images: List<MultipartFile>): MagazineResponse {
        val magazine = getMagazineOrThrow(magazineId).apply {
            val magazineImages = magazineImageRepository.saveAll(
                images.map { image ->
                    MagazineImage(
                        magazine = this,
                        isMain = false,
                        url = s3Service.upload(
                            image, getMagazineImageDir(magazineId)
                        )
                    )
                }
            )
            this.images.addAll(magazineImages)
        }
        return magazine.toResponse()
    }

    @Transactional
    fun deleteMagazineImage(magazineId: Long, imageId: Long): MagazineResponse {
        return getMagazineOrThrow(magazineId).apply {
            val magazineImage = getMagazineImageOrThrow(imageId).apply {
                s3Service.delete(this.url)
            }
            this.images.remove(magazineImage)
        }.toResponse()
    }


    @Transactional
    fun updateMagazineImage(magazineId: Long, imageId: Long, image: MultipartFile): MagazineResponse {
        return getMagazineOrThrow(magazineId).apply {
            val magazineImage = this.images.findImage(magazineId, imageId)
            s3Service.delete(magazineImage.url)
            magazineImage.url = s3Service.upload(image, getMagazineImageDir(magazineId))
        }.toResponse()
    }

    @Transactional
    fun setMagazineMainImage(magazineId: Long, imageId: Long): MagazineResponse =
        getMagazineOrThrow(magazineId).apply {
            val magazineImage = this.images.findImage(magazineId, imageId)
            this.images.forEach { it.isMain = false }
            magazineImage.isMain = true
        }.toResponse()

    @Transactional
    fun updateMagazine(magazineId: Long, request: MagazineUpdateRequest): MagazineResponse =
        getMagazineOrThrow(magazineId).apply {
            request.title?.let { title = it }
            request.author?.let { author = it }
            request.description?.let { description = it }
            request.url?.let { url = it }
            request.categoryId?.let { category = categoryService.getCategoryOrThrow(it) }
            request.tagIds?.let { tagIds ->
                // 기존꺼 삭제
                magazineTagRepository.deleteAll(this.magazineTags)
                // 새로운거 등록
                this.magazineTags = magazineTagRepository.saveAll(
                    tagIds.map { MagazineTag(magazine = this, tag = tagService.getTagOrThrow(it)) }
                )
            }
        }.toResponse()

    @Transactional
    fun deleteMagazine(magazineId: Long): Unit =
        getMagazineOrThrow(magazineId).run {
            this.images.map {
                deleteMagazineImage(magazineId, it.id)
            }
            magazineRepository.delete(this) // 나머지는 cascade 옵션에 의해 삭제
        }

    fun getMagazineImageDir(magazineId: Long) = "magazineImage/$magazineId"

    fun MutableList<MagazineImage>.findImage(magazineId: Long, imageId: Long) =
        this.find { it.id == imageId }
            ?: throw NotFoundException("magazineId: $magazineId, imageId: $imageId not exist")
}