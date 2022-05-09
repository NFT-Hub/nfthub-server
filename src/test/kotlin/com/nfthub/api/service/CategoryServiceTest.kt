package com.nfthub.api.service

import com.nfthub.api.controller.ConflictException
import com.nfthub.api.controller.NotFoundException
import com.nfthub.api.dto.CategoryResponse
import com.nfthub.api.entity.Category
import com.nfthub.api.repository.CategoryRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import javax.transaction.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
@Transactional
class CategoryServiceTest(
    @Autowired val categoryService: CategoryService,
    @Autowired val categoryRepository: CategoryRepository
) {
    @Test
    fun `getCategoryResponses`() {
        categoryRepository.save(Category(name = "category1"))
        categoryRepository.save(Category(name = "category2"))
        categoryRepository.save(Category(name = "category3"))
        val categories = categoryService.getCategoryResponses()
        assertEquals(3, categories.size)
    }

    @Test
    fun `getCategoryOrThrow - 카테고리가 없으면 throw`() {
        assertThrows<NotFoundException> {
            categoryService.getCategoryOrThrow(1L)
        }
    }

    @Test
    fun `getCategoryResponse`() {
        val savedCategory = categoryRepository.save(Category(name = "category1"))
        val categoryResponse = categoryService.getCategoryResponse(savedCategory.id)
        assertEquals(
            categoryResponse, CategoryResponse(
                id = savedCategory.id, savedCategory.name
            )
        )
    }

    @Test
    fun `getCategoryResponse - 카테고리가 없으면 throw`() {
        assertThrows<NotFoundException> {
            categoryService.getCategoryResponse(1L)
        }
    }

    @Test
    fun `createCategory`() {
        val createdCategory = categoryService.createCategory("categoryName")
        val category = categoryRepository.findByIdOrNull(createdCategory.id)
        assertNotNull(category)
    }

    @Test
    fun `createCategory - 이미 존재하는 카테고리 이름이면, throw`() {
        categoryService.createCategory("category")
        assertThrows<ConflictException> {
            categoryService.createCategory("category")
        }
    }

    @Test
    fun `updateCategoryName`() {
        val savedCategory = categoryRepository.save(Category(name = "c"))
        categoryService.updateCategoryName(savedCategory.id, "category")
        val category = categoryRepository.findByIdOrNull(savedCategory.id)
        assertEquals(category?.name, "category")
    }

    @Test
    fun `updateCategoryName - 이미 존재하는 카테고리 이름이면, throw`() {
        val savedCategory = categoryRepository.save(Category(name = "c"))
        categoryService.updateCategoryName(savedCategory.id, "category")
        assertThrows<ConflictException> {
            categoryService.updateCategoryName(savedCategory.id, "category")
        }
    }

    @Test
    fun `deleteCategory`() {
        val savedCategory = categoryRepository.save(Category(name = "c"))
        categoryService.deleteCategory(savedCategory.id)
        val category = categoryRepository.findByIdOrNull(savedCategory.id)
        assertNull(category)
    }

    @Test
    fun `deleteCategory - 카테고리가 없으면 throw`() {
        assertThrows<NotFoundException> {
            categoryService.deleteCategory(1L)
        }
    }
}