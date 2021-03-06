package com.nfthub.api.service

import com.nfthub.api.controller.ConflictException
import com.nfthub.api.controller.NotFoundException
import com.nfthub.api.dto.TagResponse
import com.nfthub.api.dto.toResponse
import com.nfthub.api.entity.Tag
import com.nfthub.api.repository.TagRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TagService(
    private val tagRepository: TagRepository
) {
    fun getTagResponses(likeKeyword: String?): List<TagResponse> {
        if (likeKeyword != null) {
            return getTagsByLikeKeyword(keyword = likeKeyword)
        }
        return tagRepository.findAll().map { it.toResponse() }
    }

    fun getTagsByLikeKeyword(keyword: String) =
        tagRepository.findAllByNameLike("%$keyword%").map { it.toResponse() }


    fun getTagOrThrow(tagId: Long) =
        tagRepository.findByIdOrNull(tagId) ?: throw NotFoundException("not found tagId: $tagId")

    fun getTagByNameOrNull(name: String): Tag? = tagRepository.findByName(name)

    fun getTagByNameOrThrow(name: String): TagResponse {
        val tag = tagRepository.findByName(name) ?: throw NotFoundException("tag not exist: name: $name ")
        return tag.toResponse()
    }

    @Transactional
    fun createTag(name: String): Tag {
        if (getTagByNameOrNull(name) != null) throw ConflictException("tag already exist: $name")
        return tagRepository.save(
            Tag(name = name)
        )
    }

    @Transactional
    fun updateTagName(tagId: Long, name: String): Tag {
        getTagByNameOrThrow(name)
        return getTagOrThrow(tagId).apply {
            this.name = name
        }
    }

    @Transactional
    fun deleteTag(tagId: Long) =
        tagRepository.delete(
            getTagOrThrow(tagId)
        )

}