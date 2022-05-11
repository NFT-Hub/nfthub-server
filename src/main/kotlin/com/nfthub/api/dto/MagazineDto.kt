package com.nfthub.api.dto

import com.nfthub.api.EMPTY_STRING
import com.nfthub.api.RequestMapper
import com.nfthub.api.ResponseMapper
import com.nfthub.api.entity.Magazine
import com.nfthub.api.entity.MagazineImage
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers


data class MagazineImageResponse(
    var id: Long = 0,
    var url: String = EMPTY_STRING,
    var isMain: Boolean = false
)

data class MagazineResponse(
    var id: Long = 0,
    var title: String = EMPTY_STRING,
    var description: String = EMPTY_STRING,
    var url: String = EMPTY_STRING,
    var author: String = EMPTY_STRING,
    var category: CategoryResponse? = null,
    var tags: List<TagResponse> = emptyList(),
    var images: List<MagazineImageResponse> = emptyList()
)

data class MagazineCreateRequest(
    var title: String = EMPTY_STRING,
    var description: String = EMPTY_STRING,
    var url: String = EMPTY_STRING,
    var author: String = EMPTY_STRING,
    var categoryId: Long? = null,
    var tagIds: List<Long>? = null
)

data class MagazineUpdateRequest(
    var title: String? = null,
    var description: String? = null,
    var url: String? = null,
    var author: String? = null,
    val categoryId: Long? = null,
    val tagIds: List<Long>? = null
)

@Mapper
interface MagazineImageMapper : ResponseMapper<MagazineImageResponse, MagazineImage> {
    companion object {
        val INSTANCE: MagazineImageMapper = Mappers.getMapper(MagazineImageMapper::class.java)
    }
}

@Mapper(uses = [CategoryMapper::class, MagazineImageMapper::class])
interface MagazineMapper : ResponseMapper<MagazineResponse, Magazine>, RequestMapper<MagazineCreateRequest, Magazine> {
    companion object {
        val INSTANCE: MagazineMapper = Mappers.getMapper(MagazineMapper::class.java)
    }
}

fun Magazine.toResponse() = MagazineMapper.INSTANCE.fromEntity(this).also { response ->
    response.tags = this.magazineTags.map { it.tag.toResponse() }
}

fun MagazineCreateRequest.toEntity() = MagazineMapper.INSTANCE.toEntity(this)

