package com.nfthub.api.service

import com.nfthub.api.controller.NotFoundException
import com.nfthub.api.dto.MagazineCreateRequest
import com.nfthub.api.dto.MagazineResponse
import com.nfthub.api.dto.MagazineUpdateRequest
import com.nfthub.api.dto.toResponse
import com.nfthub.api.entity.Category
import com.nfthub.api.entity.Magazine
import com.nfthub.api.entity.Tag
import com.nfthub.api.repository.CategoryRepository
import com.nfthub.api.repository.MagazineRepository
import com.nfthub.api.repository.TagRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import javax.persistence.EntityManager
import javax.transaction.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MagazineServiceTest(
    @Autowired val magazineService: MagazineService,
    @Autowired val magazineRepository: MagazineRepository,
    @Autowired val categoryRepository: CategoryRepository,
    @Autowired val tagRepository: TagRepository,
    @Autowired val em: EntityManager
) {
    private lateinit var categories: List<Category>
    private lateinit var tags: List<Tag>

    @MockkBean
    private lateinit var s3Service: S3Service

    @BeforeAll
    fun setup() {
        categories = categoryRepository.saveAll(
            listOf(
                Category(name = "category1"),
                Category(name = "category2")
            )
        )
        tags = tagRepository.saveAll(
            listOf(
                Tag(name = "tag1"),
                Tag(name = "tag2")
            )
        )
    }

    @Test
    fun `getMagazineOrThrow`() {
        // given
        val testMagazine = magazineRepository.save(
            Magazine()
        )
        // then
        assertDoesNotThrow {
            magazineService.getMagazineOrThrow(testMagazine.id)
        }
    }

    @Test
    fun `getMagazineOrThrow - 찾는 매거진이 없으면, throw`() {
        assertThrows<NotFoundException> {
            magazineService.getMagazineOrThrow(1L)
        }
    }

    @Test
    fun `createMagazine`() {
        // given
        val createdMagazineResponse = magazineService.createMagazine(MagazineCreateRequest(
            title = "title", description = "description", categoryId = categories[0].id,
            tagIds = tags.map { it.id }
        ))
        val magazine = magazineRepository.findByIdOrNull(createdMagazineResponse.id)
        // then
        assertEquals(magazine?.id, createdMagazineResponse.id)
        assertEquals(
            MagazineResponse(
                id = createdMagazineResponse.id,
                title = "title",
                description = "description",
                category = categories[0].toResponse(),
                tags = tags.map { it.toResponse() }
            ).toString(), createdMagazineResponse.toString()
        )
    }

    @Test
    fun `updateMagazine`() {
        val createdMagazineResponse = magazineService.createMagazine(MagazineCreateRequest(
            title = "title", description = "description",
            categoryId = categories[0].id, tagIds = tags.map { it.id }
        ))
        val magazineResponse = magazineService.updateMagazine(
            createdMagazineResponse.id, MagazineUpdateRequest(
                title = "newTitle",
                categoryId = categories[1].id,
                tagIds = listOf(tags[1].id)
            )
        )
        assertEquals(
            MagazineResponse(
                id = magazineResponse.id,
                title = "newTitle", description = "description",
                category = categories[1].toResponse(), tags = listOf(tags[1].toResponse())
            ), magazineResponse
        )
    }

    @Test
    fun `creteMagazineImage`() {
        // given
        val createdMagazine = magazineRepository.save(Magazine())
        val mockFileA = MockMultipartFile("name", "testB.png", "iamge/png", byteArrayOf(1))
        every {
            s3Service.upload(mockFileA, any())
        } returns "a"

        // when
        magazineService.createMagazineImage(createdMagazine.id, listOf(mockFileA))
        val magazine = magazineRepository.findByIdOrNull(createdMagazine.id)
        // then
        assertEquals(true, magazine?.images?.all { it.url == "a" })

    }

    @Test
    fun `updateMagazineImage`() {
        // given
        val savedMagazine = magazineRepository.save(Magazine())
        val mockFileA = MockMultipartFile("name", "testB.png", "iamge/png", byteArrayOf(1))
        val mockFileB = MockMultipartFile("name", "testB.png", "iamge/png", byteArrayOf(1))
        every {
            s3Service.upload(mockFileA, any())
        } returns "a"
        every {
            s3Service.upload(mockFileB, any())
        } returns "b"
        every {
            s3Service.delete(any())
        } returns Unit

        // when
        val createdMagazine = magazineService.createMagazineImage(savedMagazine.id, listOf(mockFileA))
        val magazine = magazineService.updateMagazineImage(createdMagazine.id, createdMagazine.images[0].id, mockFileB)

        // then
        assertEquals(true, magazine?.images?.all { it.url == "b" })
    }

    @Test
    fun `deleteMagazineImage`() {
        // given
        val savedMagazine = magazineRepository.save(Magazine())
        val mockFileA = MockMultipartFile("name", "testB.png", "iamge/png", byteArrayOf(1))
        every {
            s3Service.upload(mockFileA, any())
        } returns "a"
        every {
            s3Service.delete(any())
        } returns Unit

        // when
        val createdMagazine = magazineService.createMagazineImage(savedMagazine.id, listOf(mockFileA))
        val createdMagazineImageId = createdMagazine.images[0].id
        val magazine = magazineService.deleteMagazineImage(createdMagazine.id, createdMagazineImageId)
        assertTrue { magazine?.images?.size == 0 }

    }

    @Test
    fun `setMagazineMainImage`() {
        // given
        val savedMagazine = magazineRepository.save(Magazine())
        val mockFileA = MockMultipartFile("name", "testB.png", "iamge/png", byteArrayOf(1))
        every {
            s3Service.upload(mockFileA, any())
        } returns "a"
        every {
            s3Service.delete(any())
        } returns Unit
        magazineService.createMagazineImage(savedMagazine.id, listOf(mockFileA))
        magazineService.createMagazineImage(savedMagazine.id, listOf(mockFileA))

        val magazine = magazineService.getMagazineOrThrow(savedMagazine.id)
        val firstImageId = magazine.images[0].id
        val secondImageId = magazine.images[1].id

        // when
        magazineService.setMagazineMainImage(magazine.id, secondImageId)
        // then
        assertAll(
            { assertTrue { !magazineService.getMagazineOrThrow(magazine.id).images[0].isMain } },
            { assertTrue { magazineService.getMagazineOrThrow(magazine.id).images[1].isMain } },
        )
        // when
        magazineService.setMagazineMainImage(magazine.id, firstImageId)
        // then
        assertAll(
            { assertTrue { magazineService.getMagazineOrThrow(magazine.id).images[0].isMain } },
            { assertTrue { !magazineService.getMagazineOrThrow(magazine.id).images[1].isMain } },
        )
    }


    @Test
    fun `getMagazineResponses - param, tagIds`() {

    }

    @Test
    fun `getMagazineResponses - param, categoryId`() {

    }

    @Test
    fun `getMagazineResponses param, searchTag`() {

    }

    @Test
    fun `getMagazineResponse - param, categoryId and searchTag and tagIds`() {

    }


}