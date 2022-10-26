package com.nfthub.core.service;

import com.nfthub.core.entity.Tag;
import com.nfthub.core.exception.AlreadyExistException;
import com.nfthub.core.exception.NotFoundException;
import com.nfthub.core.repository.TagRepository;
import com.nfthub.core.response.TagResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class TagServiceTest {
    @Autowired // https://pinokio0702.tistory.com/189
    private TagService tagService;
    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("getTagsBy() - 정상적으로 조회되는지 확인")
    public void getTagsByTest() {

    }

    @Test
    @DisplayName("getTagResponses - 정상적으로 모두 조회되는지 확인")
    public void getTagResponsesTest() {
        tagService.createTagOrThrow("test1");
        tagService.createTagOrThrow("test2");
        tagService.createTagOrThrow("test3");
        Assertions.assertEquals(3, tagService.getTagsRes().size());
    }

    @Test
    @DisplayName("getTagResponsesByLikeKeyword - 정상적으로 조회되면, 유사한 키워드도 같이 반환되는지 확인")
    public void getTagResponsesWithKeywordParamTest() {
        tagService.createTagOrThrow("안녕하세요");
        tagService.createTagOrThrow("안녕하세");
        tagService.createTagOrThrow("안녕하");
        List<TagResponse> tags1 = tagService.getTagsResByLikeKeyword("녕하");
        List<TagResponse> tags2 = tagService.getTagsResByLikeKeyword("안녕하세");
        List<TagResponse> tags3 = tagService.getTagsResByLikeKeyword("세요");
        Assertions.assertEquals(3, tags1.size());
        Assertions.assertEquals(2, tags2.size());
        Assertions.assertEquals(1, tags3.size());
    }

    @Test
    @DisplayName("getTagResponseByIdOrThrow - 정상적으로 조회되는지 확인")
    public void getTagResponseByIdOrThrowTest() {
        TagResponse tag = tagService.createTagOrThrow("test");
        TagResponse tagResponse = tagService.getTagResByIdOrThrow(tag.getId());
        Assertions.assertEquals(tag.getId(), tagResponse.getId());
    }

    @Test
    @DisplayName("getTagResponseByNameOrThrow - 정상적으로 조회되는지 확인")
    public void getTagResponseByNameOrThrowTest() {
        TagResponse tag = tagService.createTagOrThrow("test");
        TagResponse tagResponse = tagService.getTagResByNameOrThrow(tag.getName());
        Assertions.assertEquals(tag.getName(), tagResponse.getName());
    }

    @Test
    @DisplayName("createTagOrThrow - 정상적으로 생성되는지 확인")
    public void createTagTest() {
        TagResponse savedTag = tagService.createTagOrThrow("test");
        Optional<Tag> findTag = tagRepository.findById(savedTag.getId());
        Assertions.assertTrue(findTag.isPresent());
    }

    @Test
    @DisplayName("createTagOrThrow - 이미 같은 이름이 있다면 throw")
    public void createTagOrThrowTest() {
        tagService.createTagOrThrow("test");
        Assertions.assertThrows(AlreadyExistException.class, () -> {
            tagService.createTagOrThrow("test");
        });
    }

    @Test
    @DisplayName("updateTagOrThrow - 정상적으로 수정되는지 확인")
    public void updateTagOrThrowTest() {
        TagResponse savedTag = tagService.createTagOrThrow("test");
        tagService.updateTagOrThrow(savedTag.getId(), "test2");
        Assertions.assertEquals("test2", tagService.getTagResByIdOrThrow(savedTag.getId()).getName());
    }

    @Test
    @DisplayName("updateTagOrThrow - 키워드가 이미 존재한다면 throw")
    public void updateTagOrThrowTest2() {
        TagResponse savedTag = tagService.createTagOrThrow("test");
        tagService.createTagOrThrow("test2");
        Assertions.assertThrows(AlreadyExistException.class, () -> {
            tagService.updateTagOrThrow(savedTag.getId(), "test2");
        });
    }

    @Test
    @DisplayName("deleteTagOrThrow - 정상적으로 삭제되는지 확인")
    public void deleteTagOrThrow() {
        TagResponse tag = tagService.createTagOrThrow("test");
        tagService.deleteTagOrThrow(tag.getId());
        Assertions.assertThrows(NotFoundException.class, () -> {
            tagService.getTagResByIdOrThrow(tag.getId());
        });
    }
}
