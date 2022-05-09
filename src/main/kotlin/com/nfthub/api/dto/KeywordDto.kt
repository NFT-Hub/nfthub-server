package com.nfthub.api.dto

import com.nfthub.api.EMPTY_STRING
import com.nfthub.api.ResponseMapper
import com.nfthub.api.entity.Keyword
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers


data class KeywordResponse(
    var id: Long = 0,
    var name: String = EMPTY_STRING
)

@Mapper
interface KeywordMapper : ResponseMapper<KeywordResponse, Keyword> {
    companion object {
        val INSTANCE: KeywordMapper = Mappers.getMapper(KeywordMapper::class.java)
    }
}

fun Keyword.toResponse() = KeywordMapper.INSTANCE.fromEntity(this)