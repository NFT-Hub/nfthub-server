package com.nfthub.api.service

import com.nfthub.api.controller.NotFoundException
import com.nfthub.api.dto.MagazineCreateRequest
import com.nfthub.api.dto.MagazineResponse
import com.nfthub.api.dto.MagazineUpdateRequest
import com.nfthub.api.dto.toResponse
import com.nfthub.api.entity.Category
import com.nfthub.api.entity.Keyword
import com.nfthub.api.entity.Magazine
import com.nfthub.api.repository.CategoryRepository
import com.nfthub.api.repository.KeywordRepository
import com.nfthub.api.repository.MagazineRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import javax.transaction.Transactional
import kotlin.test.assertEquals

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MagazineServiceTest(
    @Autowired val magazineService: MagazineService,
    @Autowired val magazineRepository: MagazineRepository,
    @Autowired val categoryRepository: CategoryRepository,
    @Autowired val keywordRepository: KeywordRepository
) {
    private lateinit var categories: List<Category>
    private lateinit var keywords: List<Keyword>

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
        keywords = keywordRepository.saveAll(
            listOf(
                Keyword(name = "keyword1"),
                Keyword(name = "keyword2")
            )
        )
    }

    @Test
    fun `getMagazineOrThrow`() {
        val testMagazine = magazineRepository.save(
            Magazine()
        )
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
        val createdMagazineResponse = magazineService.createMagazine(MagazineCreateRequest(
            title = "title", description = "description", categoryId = categories[0].id,
            keywordIds = keywords.map { it.id }
        ))
        val magazine = magazineRepository.findByIdOrNull(createdMagazineResponse.id)
        assertEquals(magazine?.id, createdMagazineResponse.id)
        assertEquals(
            MagazineResponse(
                id = createdMagazineResponse.id,
                title = "title",
                description = "description",
                category = categories[0].toResponse(),
                keywords = keywords
            ), createdMagazineResponse
        )
    }

    @Test
    fun `updateMagazine`() {
        val createdMagazineResponse = magazineService.createMagazine(MagazineCreateRequest(
            title = "title", description = "description",
            categoryId = categories[0].id, keywordIds = keywords.map { it.id }
        ))
        val magazineResponse = magazineService.updateMagazine(
            createdMagazineResponse.id, MagazineUpdateRequest(
                title = "newTitle",
                categoryId = categories[1].id,
                keywordIds = listOf(keywords[1].id)
            )
        )
        assertEquals(
            MagazineResponse(
                title = "newTitle", description = "description",
                category = categories[1].toResponse(), keywords = listOf(keywords[1])
            ), magazineResponse
        )
    }

    @Test
    fun `updateMagazineKeywords`() {
        val createdMagazine = magazineRepository.save(
            Magazine()
        )
        magazineService.updateMagazineKeywords(
            createdMagazine.id, keywords.map { it.id }
        )
        val magazineRes = magazineService.getMagazineOrThrow(createdMagazine.id)
        assertEquals(
            MagazineResponse(
                id = createdMagazine.id,
                title = "title",
                keywords = keywords
            ), magazineRes
        )
    }

    @Test
    fun `updateMagazineKeywords - 키워드가 없으면 throw`() {
        val createdMagazine = magazineRepository.save(
            Magazine()
        )
        assertThrows<NotFoundException> {
            magazineService.updateMagazineKeywords(
                createdMagazine.id, keywords.map { it.id + 10 }
            )
        }
    }

    @Test
    fun `updateMagazineCategory`() {
        val createdMagazine = magazineRepository.save(
            Magazine()
        )
        magazineService.updateMagazineCategory(createdMagazine.id, categories[0].id)
        val magazineRes = magazineRepository.findByIdOrNull(createdMagazine.id)
        assertEquals(categories[0].id, magazineRes?.category?.id)
    }

    @Test
    fun `creteMagazineImage`() {
        // given
        val createdMagazine = magazineRepository.save(
            Magazine()
        )
        val mockFileA = MockMultipartFile("name", "testB.png", "iamge/png", byteArrayOf(1))
        every {
            s3Service.upload(mockFileA, any())
        } returns "a"

        // when
        magazineService.createMagazineImage(createdMagazine.id, mockFileA)
        val magazine = magazineRepository.findByIdOrNull(createdMagazine.id)
        // then
        assertEquals(true, magazine?.images?.all { it.url == "a" })

    }

    @Test
    fun `updateMagazineImage`() {

    }

    @Test
    fun `deleteMagazineImage`() {
    }

    @Test
    fun `setMagazineMainImage`() {

    }


    @Test
    fun `getMagazineResponses - param, keywordIds`() {

    }

    @Test
    fun `getMagazineResponses - param, categoryId`() {

    }

    @Test
    fun `getMagazineResponses param, searchKeyword`() {

    }

    @Test
    fun `getMagazineResponse - param, categoryId and searchKeyword and keywordIds`() {

    }


}