package com.nfthub.api.service

import com.nfthub.api.controller.ConflictException
import com.nfthub.api.controller.NotFoundException
import com.nfthub.api.entity.Keyword
import com.nfthub.api.repository.KeywordRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class KeywordService(
    private val keywordRepository: KeywordRepository
) {
    fun getKeywordResponses(): List<Keyword> =
        keywordRepository.findAll()

    fun getKeywordOrThrow(keywordId: Long) =
        keywordRepository.findByIdOrNull(keywordId) ?: throw NotFoundException("not found keywordId: $keywordId")

    fun existKeywordNameOrThrow(name: String) {
        val keyword = keywordRepository.findByName(name)
        if (keyword != null) {
            throw ConflictException("already exist keyword name: keywordId: ${keyword.id}, name: $name ")
        }
    }

    @Transactional
    fun createKeyword(name: String): Keyword {
        existKeywordNameOrThrow(name)
        return keywordRepository.save(
            Keyword(name = name)
        )
    }

    @Transactional
    fun updateKeywordName(keywordId: Long, name: String): Keyword {
        existKeywordNameOrThrow(name)
        return getKeywordOrThrow(keywordId).apply {
            this.name = name
        }
    }

    @Transactional
    fun deleteKeyword(keywordId: Long) =
        keywordRepository.delete(
            getKeywordOrThrow(keywordId)
        )

}