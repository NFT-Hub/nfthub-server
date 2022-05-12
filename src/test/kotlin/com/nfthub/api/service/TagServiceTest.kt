package com.nfthub.api.service

import com.nfthub.api.controller.ConflictException
import com.nfthub.api.controller.NotFoundException
import com.nfthub.api.entity.Tag
import com.nfthub.api.repository.TagRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@Transactional
class TagServiceTest(
    @Autowired val tagService: TagService,
    @Autowired val tagRepository: TagRepository
) {
    @Test
    fun `createTag - 정상적으로 생성되는지 확인`() {
        val savedTag = tagService.createTag("tag")
        val tag = tagRepository.findById(savedTag.id)
        assertNotNull(tag)
    }

    @Test
    fun `createTag - 이미 같은 이름이 있다면, throw`() {
        val testName = "tag"
        tagService.createTag(testName)
        assertThrows<ConflictException> {
            tagService.createTag(testName)
        }
    }

    @Test
    fun `getTagResponses - 정상적으로 모두 조회되는지 확인`() {
        //val tags
        tagRepository.save(Tag(name = "tag1"))
        tagRepository.save(Tag(name = "tag2"))
        tagRepository.save(Tag(name = "tag3"))
        val list = tagService.getTagResponses(null)
        assertEquals(3, list.size)

    }

    @Test
    fun `getTagResponses param keyword - 정상적으로 조회되면, 유사한 키워드 추천`() {
        tagRepository.save(Tag(name = "안녕하세요"))
        tagRepository.save(Tag(name = "안녕하세"))
        tagRepository.save(Tag(name = "안녕하"))
        val tags1 = tagService.getTagResponses(likeKeyword = "녕하")
        val tags2 = tagService.getTagResponses(likeKeyword = "안녕하세")
        val tags3 = tagService.getTagResponses(likeKeyword = "세요")
        assertEquals(3, tags1.size)
        assertEquals(2, tags2.size)
        assertEquals(1, tags3.size)

    }

    @Test
    fun `getTagOrThrow - 키워드가 없다면, throw`() {
        assertThrows<NotFoundException> {
            tagService.getTagOrThrow(1L)
        }
    }

    @Test
    fun `updateTagName - 정상적으로 업데이트 되는지 확인`() {
        val savedTag = tagRepository.save(Tag(name = "tag"))
        tagService.updateTagName(savedTag.id, "testTag")
        val tag = tagService.getTagOrThrow(savedTag.id)
        assertEquals("testTag", savedTag.name)
        assertEquals("testTag", tag.name)
    }

    @Test
    fun `updateTagName - 키워드가 이미 존재한다면, throw`() {
        val tag = tagRepository.save(Tag(name = "tag"))
        assertThrows<ConflictException> {
            tagService.updateTagName(tag.id, "tag")
        }
    }

    @Test
    fun `deleteTag - 정상적으로 작동하는지 확인`() {
        val tag = tagRepository.save(Tag(name = "tag"))
        tagService.deleteTag(tag.id)
        assertThrows<NotFoundException> {
            tagService.getTagOrThrow(tag.id)
        }
    }
}