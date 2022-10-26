package com.nfthub.core.service;

import com.nfthub.core.entity.Category;
import com.nfthub.core.entity.CategoryGroup;
import com.nfthub.core.exception.AlreadyExistException;
import com.nfthub.core.exception.NotFoundException;
import com.nfthub.core.repository.CategoryGroupRepository;
import com.nfthub.core.repository.CategoryRepository;
import com.nfthub.core.response.CategoryGroupResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryGroupServiceTest {
    List<Category> categories;
    @Autowired
    private CategoryGroupService categoryGroupService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryGroupRepository categoryGroupRepository;

    @BeforeEach
    public void setUp() {
        Category category1 = new Category();
        category1.setName("category1");
        categoryRepository.save(category1);
        Category category2 = new Category();
        category2.setName("category2");
        categoryRepository.save(category2);
        categories = List.of(category1, category2);
    }

    @Test
    @DisplayName("categoryGroupRes - 카테고리 그룹 조회")
    public void getCategoryGroupsResTest() {
        CategoryGroup categoryGroup1 = new CategoryGroup();
        categoryGroup1.setName("categoryGroup1");
        categoryGroupRepository.save(categoryGroup1);
        CategoryGroup categoryGroup2 = new CategoryGroup();
        categoryGroup2.setName("categoryGroup2");
        categoryGroupRepository.save(categoryGroup2);
        List<CategoryGroupResponse> list = categoryGroupService.getCategoryGroupsRes();
        Assertions.assertEquals(2, list.size());
    }

    @Test
    @DisplayName("categoryGroupRes - 카테고리 그룹 없으면 throw")
    public void getCategoryGroupTest_throw() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            categoryGroupService.getCategoryGroupResOrThrow(1L);
        });
    }

    @Test
    @DisplayName("createCategoryGroup - 정상적으로 생성되는지 확인")
    public void createCategoryGroupTest() {
        CategoryGroupResponse response = categoryGroupService.createCategoryGroup("categoryGroup");
        Assertions.assertTrue(categoryGroupRepository.findById(response.getId()).isPresent());
    }

    @Test
    @DisplayName("createCategoryGroup - 이미 존재한다면 throw")
    public void createCategoryGroupTest_throw() {
        categoryGroupService.createCategoryGroup("categoryGroup");
        Assertions.assertThrows(AlreadyExistException.class, () -> {
            categoryGroupService.createCategoryGroup("categoryGroup");
        });
    }

    @Test
    @DisplayName("updateCategoryGroup - 정상적으로 수정되는지 확인")
    public void updateCategoryGroupTest() {
        CategoryGroupResponse response = categoryGroupService.createCategoryGroup("categoryGroup");
        Assertions.assertEquals(response.getId(), categoryGroupRepository.findById(response.getId()).get().getId());
    }

    @Test
    @DisplayName("updateCategoryGroup - 이미 존재하는 카테고리 이름이면 throw")
    public void updateCategoryGroupTest_throw() {
        CategoryGroupResponse response = categoryGroupService.createCategoryGroup("categoryGroup");
        categoryGroupService.createCategoryGroup("categoryGroup2");
        Assertions.assertThrows(AlreadyExistException.class, () -> {
            categoryGroupService.updateCategoryGroup(response.getId(), "categoryGroup2");
        });
    }

    @Test
    @DisplayName("deleteCategoryGroup - 정상적으로 삭제되는지 확인")
    public void deleteCategoryGroupTest() {
        CategoryGroupResponse response = categoryGroupService.createCategoryGroup("categoryGroup");
        categoryGroupService.deleteCategoryGroup(response.getId());
        Assertions.assertFalse(categoryGroupRepository.findById(response.getId()).isPresent());
    }

    @Test
    @DisplayName("addCategoryToCategoryGroup - 정상적으로 추가되는지 확인")
    public void addCategoryToCategoryGroupTest() {
        CategoryGroupResponse response = categoryGroupService.createCategoryGroup("categoryGroup");
        CategoryGroupResponse categoryResponse =
                categoryGroupService.addCategoryToGroup(
                        response.getId(),
                        categories.stream().map(Category::getId).collect(Collectors.toList())
                );
        Assertions.assertEquals(categories.size(), categoryResponse.getCategories().size());
    }
}
