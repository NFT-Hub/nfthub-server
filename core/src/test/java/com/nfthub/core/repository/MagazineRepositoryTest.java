package com.nfthub.core.repository;

import com.nfthub.core.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
public class MagazineRepositoryTest {
    List<Tag> tags;
    List<Category> categories;
    List<CategoryGroup> categoryGroups;
    @Autowired
    private MagazineRepository magazineRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryGroupRepository categoryGroupRepository;
    @Autowired
    private MagazineTagRepository magazineTagRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void beforeEach() {
        tags = tagRepository.saveAll(List.of(
                new Tag("tag1"),
                new Tag("tag2"),
                new Tag("tag3")
        ));
        categoryGroups = categoryGroupRepository.saveAll(List.of(
                new CategoryGroup("categoryGroup1"),
                new CategoryGroup("categoryGroup2"),
                new CategoryGroup("categoryGroup3")
        ));
        categories = categoryRepository.saveAll(List.of(
                new Category("category1"),
                new Category("category2"),
                new Category("category3")
        ));
        categories.get(0).setCategoryGroup(categoryGroups.get(0));
        categories.get(1).setCategoryGroup(categoryGroups.get(1));
        // @BeforeAll 일때는 필요하지만, @BeforeEach 일때는 필요없음
        //categoryRepository.saveAll(categories);
        //categoryGroupRepository.saveAll(categoryGroups);
    }

    @Test
    @DisplayName("findAllBy - simple 매거진 조회 테스트")
    @Transactional
    public void findAllBy() {
        Magazine magazine = getSavedMagazineWithTitle("title");
        saveTags(magazine, tags);
        magazine.setCategory(categories.get(0));
        Pageable pageable = PageRequest.of(0, 10);

        List<CategoryGroup> categoryGroups = categoryGroupRepository.findAll();
        Page<Magazine> result = magazineRepository.findAllBy(
                pageable,
                List.of(tags.get(0).getId(), tags.get(1).getId()),
                List.of(categories.get(0).getId(), categories.get(1).getId()),
                List.of(categoryGroups.get(0).getId(), categoryGroups.get(1).getId()),
                null,
                null,
                null,
                null,
                null
        );
        Assertions.assertEquals(magazine.getId(), result.getContent().get(0).getId());
        Assertions.assertEquals(1, result.getContent().size());

        Page<Magazine> result2 = magazineRepository.findAllBy(
                pageable,
                null, null, null,
                "nfthub",
                null,
                null,
                null,
                null
        );
        Assertions.assertEquals(0, result2.getContent().size());
    }

    @Test
    @DisplayName("findAllBy - with keyword")
    @Transactional
    public void findAllBy2() {
        Magazine magazine = getSavedMagazineWithTitle("nfthub");
        saveTags(magazine, tags);
        magazine.setCategory(categories.get(0));
        Pageable pageable = PageRequest.of(0, 10);

        Page<Magazine> result = magazineRepository.findAllBy(
                pageable,
                null,
                null,
                null,
                "nft",
                null,
                null,
                null,
                null
        );
        Assertions.assertEquals(magazine.getId(), result.getContent().get(0).getId());
        Assertions.assertEquals(1, result.getContent().size());
    }

    private Magazine getSavedMagazineWithTitle(String title) {
        Magazine magazine = new Magazine(title, "content", "author", "thumbnail");
        magazineRepository.save(magazine);
        magazine.setCategory(categories.get(0));
        return magazine;
    }

    private void saveTags(Magazine magazine, List<Tag> tags) {
        tags.forEach((tag) -> {
            MagazineTag magazineTag = new MagazineTag();
            magazineTag.setTag(tag);
            magazineTag.setMagazine(magazine);
            magazineTagRepository.save(magazineTag);
        });
    }

    @Test
    @DisplayName("findAllBy - with tag and category and keyword")
    @Transactional
    public void findAllBy3() {
        Magazine magazine = getSavedMagazineWithTitle("nfthub");
        saveTags(magazine, tags);
        magazine.setCategory(categories.get(0));
        Pageable pageable = PageRequest.of(0, 10);

        Page<Magazine> result = magazineRepository.findAllBy(
                pageable,
                List.of(tags.get(0).getId(), tags.get(1).getId()),
                List.of(categories.get(0).getId(), categories.get(1).getId()),
                null,
                "nft",
                null,
                null,
                null,
                null
        );
        Assertions.assertEquals(magazine.getId(), result.getContent().get(0).getId());
        Assertions.assertEquals(1, result.getContent().size());
    }
}
