package com.nfthub.api.service

import com.nfthub.api.controller.ConflictException
import com.nfthub.api.controller.NotFoundException
import com.nfthub.api.dto.toResponse
import com.nfthub.api.entity.Category
import com.nfthub.api.entity.CategoryGroup
import com.nfthub.api.repository.CategoryGroupRepository
import com.nfthub.api.repository.CategoryRepository
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import javax.transaction.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertNull

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryGroupServiceTest(
    @Autowired val categoryGroupService: CategoryGroupService,
    @Autowired val categoryGroupRepository: CategoryGroupRepository,
    @Autowired val categoryRepository: CategoryRepository
) {

    lateinit var categories: List<Category>

    @BeforeEach
    fun setup() {
        categories = categoryRepository.saveAll(
            listOf(
                Category(name = "category1"),
                Category(name = "category2")
            )
        )
    }

    @Test
    fun `getCategoryGroupResponses - 정상적으로 조회`() {
        categoryGroupRepository.save(CategoryGroup(name = "categoryGroup"))
        categoryGroupRepository.save(CategoryGroup(name = "categoryGroup2"))
        val categoryGroupResponses = categoryGroupService.getCategoryGroupResponses()
        assertEquals(2, categoryGroupResponses.size)
    }

    @Test
    fun `getCategoryGroupOrThrow - 정상적으로 조회`() {
        val savedCategoryGroup = categoryGroupRepository.save(CategoryGroup(name = "categoryGroup"))
        val categoryGroup = categoryGroupService.getCategoryGroupOrThrow(savedCategoryGroup.id)
        assertEquals(savedCategoryGroup, categoryGroup)
    }

    @Test
    fun `getCategoryGroupOrThrow - 카테고리 그룹이 없으면, throw`() {
        assertThrows<NotFoundException> {
            categoryGroupService.getCategoryGroupOrThrow(1L)
        }
    }

    @Test
    fun `createCategoryGroup - 정상적으로 생성시, 조회 가능`() {
        val createdCategoryGroupRes = categoryGroupService.createCategoryGroup("categoryGroup")
        val categoryGroup = categoryGroupService.getCategoryGroupOrThrow(createdCategoryGroupRes.id)
        assertEquals(createdCategoryGroupRes, categoryGroup.toResponse())
    }

    @Test
    fun `createCategoryGroup - 이미 존재하는 카테고리 그룹 이름이면, throw`() {
        categoryGroupService.createCategoryGroup("categoryGroup")
        assertThrows<ConflictException> {
            categoryGroupService.createCategoryGroup("categoryGroup")
        }
    }

    @Test
    fun `updateCategoryGroupName - 정상적으로 수정시, 반영`() {
        val savedCategoryGroup = categoryGroupRepository.save(
            CategoryGroup(name = "categoryGroup")
        )
        categoryGroupService.updateCategoryGroupName(savedCategoryGroup.id, "newCategoryGroup")
        val response = categoryGroupService.getCategoryGroupResponse(savedCategoryGroup.id)
        assertEquals("newCategoryGroup", response.name)
    }

    @Test
    fun `updateCategoryGroupName - 이미 존재하는 카테고리 그룹이면, throw`() {
        val savedCategoryGroup = categoryGroupRepository.save(
            CategoryGroup(name = "categoryGroup")
        )
        assertThrows<ConflictException> {
            categoryGroupService.updateCategoryGroupName(savedCategoryGroup.id, "categoryGroup")
        }

    }

    @Test
    fun `deleteCategory - 정상적으로 삭제시, 조회되지 않아야 함`() {
        val savedCategoryGroup = categoryGroupRepository.save(
            CategoryGroup(name = "categoryGroup")
        )
        categoryGroupService.deleteCategoryGroup(savedCategoryGroup.id)
        val category = categoryGroupRepository.findByIdOrNull(savedCategoryGroup.id)
        assertNull(category)
    }

    @Test
    fun `addCategoryToCategoryGroup - 인자로 들어온 categoryIds 카테고리 그룹에 반영되는지 확인`() {
        val savedCategoryGroup = categoryGroupRepository.save(
            CategoryGroup(name = "categoryGroup")
        )
        val categoryGroupRes = categoryGroupService.addCategoryToCategoryGroup(
            savedCategoryGroup.id,
            categoryIds = categories.map { it.id }
        )
        assertAll(
            { assertEquals(savedCategoryGroup.name, categoryGroupRes.name) },
            { assertEquals(savedCategoryGroup.categories.map { it.id }, categoryGroupRes.categories.map { it.id }) }
        )
    }

    @Test
    fun `addCategoryToCategoryGroup - 요청으로 들어온 카테고리가 없으면, throw`() {
        val savedCategoryGroup = categoryGroupRepository.save(
            CategoryGroup(name = "categoryGroup")
        )
        assertThrows<NotFoundException> {
            categoryGroupService.addCategoryToCategoryGroup(
                savedCategoryGroup.id,
                categoryIds = categories.map { it.id + 10 }
            )
        }
    }

}