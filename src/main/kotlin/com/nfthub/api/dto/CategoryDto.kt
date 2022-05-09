package com.nfthub.api.dto

import com.nfthub.api.EMPTY_STRING
import com.nfthub.api.ResponseMapper
import com.nfthub.api.entity.Category
import com.nfthub.api.entity.CategoryGroup
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

// category
data class CategoryResponse(
    var id: Long = 0,
    var name: String = EMPTY_STRING,
)

@Mapper
interface CategoryMapper : ResponseMapper<CategoryResponse, Category> {
    companion object {
        val INSTANCE: CategoryMapper = Mappers.getMapper(CategoryMapper::class.java)
    }
}

fun Category.toResponse() = CategoryMapper.INSTANCE.fromEntity(this)

// categoryGroup
data class CategoryGroupResponse(
    var id: Long = 0,
    var name: String = EMPTY_STRING,
    var categories: List<CategoryResponse> = emptyList()
)

@Mapper(uses = [CategoryMapper::class])
interface CategoryGroupMapper : ResponseMapper<CategoryGroupResponse, CategoryGroup> {
    companion object {
        val INSTANCE: CategoryGroupMapper = Mappers.getMapper(CategoryGroupMapper::class.java)
    }
}

fun CategoryGroup.toResponse() = CategoryGroupMapper.INSTANCE.fromEntity(this)