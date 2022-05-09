package com.nfthub.api.service

import com.nfthub.api.controller.ConflictException
import com.nfthub.api.controller.NotFoundException
import com.nfthub.api.entity.Keyword
import com.nfthub.api.repository.KeywordRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@Transactional
class KeywordServiceTest(
    @Autowired val keywordService: KeywordService,
    @Autowired val keywordRepository: KeywordRepository
) {
    @Test
    fun `createKeyword - 정상적으로 생성되는지 확인`() {
        val savedKeyword = keywordService.createKeyword("keyword")
        val keyword = keywordRepository.findById(savedKeyword.id)
        assertNotNull(keyword)
    }

    @Test
    fun `createKeyword - 이미 같은 이름이 있다면, throw`() {
        val testName = "keyword"
        keywordService.createKeyword(testName)
        assertThrows<ConflictException> {
            keywordService.createKeyword(testName)
        }
    }

    @Test
    fun `getKeywordResponses - 정상적으로 모두 조회되는지 확인`() {
        //val keywords
        keywordRepository.save(Keyword(name = "keyword1"))
        keywordRepository.save(Keyword(name = "keyword2"))
        keywordRepository.save(Keyword(name = "keyword3"))
        val list = keywordService.getKeywordResponses()
        assertEquals(3, list.size)

    }

    @Test
    fun `getKeywordOrThrow - 키워드가 없다면, throw`() {
        assertThrows<NotFoundException> {
            keywordService.getKeywordOrThrow(1L)
        }
    }

    @Test
    fun `updateKeywordName - 정상적으로 업데이트 되는지 확인`() {
        val savedKeyword = keywordRepository.save(Keyword(name = "keyword"))
        keywordService.updateKeywordName(savedKeyword.id, "testKeyword")
        val keyword = keywordService.getKeywordOrThrow(savedKeyword.id)
        assertEquals("testKeyword", savedKeyword.name)
        assertEquals("testKeyword", keyword.name)
    }

    @Test
    fun `updateKeywordName - 키워드가 이미 존재한다면, throw`() {
        val keyword = keywordRepository.save(Keyword(name = "keyword"))
        assertThrows<ConflictException> {
            keywordService.updateKeywordName(keyword.id, "keyword")
        }
    }

    @Test
    fun `deleteKeyword - 정상적으로 작동하는지 확인`() {
        val keyword = keywordRepository.save(Keyword(name = "keyword"))
        keywordService.deleteKeyword(keyword.id)
        assertThrows<NotFoundException> {
            keywordService.getKeywordOrThrow(keyword.id)
        }
    }
}