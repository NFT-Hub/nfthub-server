package com.nfthub.api.service

import com.nfthub.api.controller.ConflictException
import com.nfthub.api.controller.NotFoundException
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
    fun getTagResponses(): List<Tag> =
        tagRepository.findAll()

    fun getTagOrThrow(tagId: Long) =
        tagRepository.findByIdOrNull(tagId) ?: throw NotFoundException("not found tagId: $tagId")

    fun existTagNameOrThrow(name: String) {
        val tag = tagRepository.findByName(name)
        if (tag != null) {
            throw ConflictException("already exist tag name: tagId: ${tag.id}, name: $name ")
        }
    }

    @Transactional
    fun createTag(name: String): Tag {
        existTagNameOrThrow(name)
        return tagRepository.save(
            Tag(name = name)
        )
    }

    @Transactional
    fun updateTagName(tagId: Long, name: String): Tag {
        existTagNameOrThrow(name)
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