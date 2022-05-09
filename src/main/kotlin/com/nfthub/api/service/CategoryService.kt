package com.nfthub.api.service

import com.nfthub.api.controller.ConflictException
import com.nfthub.api.controller.NotFoundException
import com.nfthub.api.dto.CategoryGroupResponse
import com.nfthub.api.dto.CategoryResponse
import com.nfthub.api.dto.toResponse
import com.nfthub.api.entity.Category
import com.nfthub.api.entity.CategoryGroup
import com.nfthub.api.repository.CategoryGroupRepository
import com.nfthub.api.repository.CategoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    fun getCategoryResponses() =
        categoryRepository.findAll().map { it.toResponse() }

    fun getCategoryOrThrow(categoryId: Long) =
        categoryRepository.findByIdOrNull(categoryId) ?: throw NotFoundException("not found categoryId: $categoryId")

    fun getCategoryResponse(categoryId: Long) =
        getCategoryOrThrow(categoryId).toResponse()

    fun getCategoryByNameOrThrowIfExist(name: String) {
        val category = categoryRepository.findByName(name)
        if (category != null) {
            throw ConflictException("already exist category name: ${category.id}, ${category.name}")
        }
    }

    @Transactional
    fun createCategory(name: String): CategoryResponse {
        getCategoryByNameOrThrowIfExist(name)
        return categoryRepository.save(
            Category(name = name),
        ).toResponse()
    }

    @Transactional
    fun updateCategoryName(categoryId: Long, name: String): CategoryResponse {
        getCategoryByNameOrThrowIfExist(name)
        return categoryRepository.save(
            getCategoryOrThrow(categoryId).apply {
                this.name = name
            }
        ).toResponse()
    }

    @Transactional
    fun deleteCategory(categoryId: Long) =
        categoryRepository.delete(
            getCategoryOrThrow(categoryId)
        )
}

@Service
@Transactional(readOnly = true)
class CategoryGroupService(
    private val categoryService: CategoryService,
    private val categoryGroupRepository: CategoryGroupRepository
) {
    fun getCategoryGroupOrThrow(categoryGroupId: Long) =
        categoryGroupRepository.findByIdOrNull(categoryGroupId)
            ?: throw NotFoundException("not found categoryGroupId: $categoryGroupId")

    fun getCategoryGroupResponse(categoryGroupId: Long) =
        getCategoryGroupOrThrow(categoryGroupId).toResponse()

    fun getCategoryGroupResponses() =
        categoryGroupRepository.findAll().map { it.toResponse() }

    private fun getCategoryGroupByNameOrThrowIfExist(name: String) {
        val categoryGroup = categoryGroupRepository.findByName(name)
        if (categoryGroup != null) {
            throw ConflictException("already categoryGroup name exist: ${categoryGroup.id} ${categoryGroup.name}")
        }
    }

    @Transactional
    fun createCategoryGroup(name: String): CategoryGroupResponse {
        getCategoryGroupByNameOrThrowIfExist(name)
        return categoryGroupRepository.save(
            CategoryGroup(name = name)
        ).toResponse()
    }


    @Transactional
    fun addCategoryToCategoryGroup(categoryGroupId: Long, categoryIds: List<Long>) =
        categoryGroupRepository.save(
            getCategoryGroupOrThrow(categoryGroupId).apply {
                categories = categoryIds.map {
                    categoryService.getCategoryOrThrow(it)
                }
            }
        ).toResponse()

    @Transactional
    fun updateCategoryGroupName(categoryGroupId: Long, name: String): CategoryGroupResponse {
        getCategoryGroupByNameOrThrowIfExist(name)
        return categoryGroupRepository.save(
            getCategoryGroupOrThrow(categoryGroupId).apply {
                this.name = name
            }
        ).toResponse()
    }


    @Transactional
    fun deleteCategoryGroup(categoryGroupId: Long) =
        categoryGroupRepository.delete(
            getCategoryGroupOrThrow(categoryGroupId)
        )
}