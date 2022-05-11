package com.nfthub.api.dto

import com.nfthub.api.EMPTY_STRING
import com.nfthub.api.ResponseMapper
import com.nfthub.api.entity.Tag
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers


data class TagResponse(
    var id: Long = 0,
    var name: String = EMPTY_STRING
)

@Mapper
interface TagMapper : ResponseMapper<TagResponse, Tag> {
    companion object {
        val INSTANCE: TagMapper = Mappers.getMapper(TagMapper::class.java)
    }
}

fun Tag.toResponse() = TagMapper.INSTANCE.fromEntity(this)