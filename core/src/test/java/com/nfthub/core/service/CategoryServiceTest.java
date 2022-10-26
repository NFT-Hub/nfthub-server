package com.nfthub.core.service;

import com.nfthub.core.entity.Category;
import com.nfthub.core.exception.AlreadyExistException;
import com.nfthub.core.exception.NotFoundException;
import com.nfthub.core.repository.CategoryRepository;
import com.nfthub.core.response.CategoryResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("getCategoriesRes - 카테고리 목록 조회")
    public void getCategoriesRes() {
        categoryService.createCategory("test");
        categoryService.createCategory("test2");
        List<CategoryResponse> categories = categoryService.getCategoriesRes();
        Assertions.assertEquals(2, categories.size());
    }

    @Test
    @DisplayName("getCegoryResByNameOrThrow - 이름으로 카테고리 조회")
    public void getCategoryResByNameOrThrow() {
        CategoryResponse savedCategoryRes = categoryService.createCategory("test");
        CategoryResponse categoryRes = categoryService.getCategoryResByNameOrThrow("test");
        Assertions.assertEquals(savedCategoryRes.getId(), categoryRes.getId());
    }

    @Test
    @DisplayName("createCategory - 카테고리 생성")
    public void createCategory() {
        CategoryResponse createdCategoryRes = categoryService.createCategory("test");
        Category category = categoryRepository.findById(createdCategoryRes.getId()).get();
        Assertions.assertEquals(createdCategoryRes.getId(), category.getId());
    }

    @Test
    @DisplayName("createCateogory - 이미 같은 이름이 존재하면 throw")
    public void createCategoryAlreadyExist() {
        categoryService.createCategory("test");
        Assertions.assertThrows(AlreadyExistException.class, () -> categoryService.createCategory("test"));
    }

    @Test
    @DisplayName("updateCateogory - 카테고리 수정")
    public void updateCategory() {
        CategoryResponse createdCategoryRes = categoryService.createCategory("test");
        CategoryResponse updatedCategoryRes = categoryService.updateCategory(createdCategoryRes.getId(), "test2");
        Category category = categoryRepository.findById(updatedCategoryRes.getId()).get();
        Assertions.assertEquals("test2", category.getName());
    }

    @Test
    @DisplayName("updateCateogory - 이미 같은 이름이 존재하면 throw")
    public void updateCategoryAlreadyExist() {
        CategoryResponse res = categoryService.createCategory("test");
        categoryService.createCategory("test2");
        Assertions.assertThrows(AlreadyExistException.class, () -> categoryService.updateCategory(res.getId(), "test2"));
    }

    @Test
    @DisplayName("deleteCategory - 카테고리 삭제")
    public void deleteCategory() {
        CategoryResponse createdCategoryRes = categoryService.createCategory("test");
        categoryService.deleteCategory(createdCategoryRes.getId());
        Assertions.assertFalse(categoryRepository.findById(createdCategoryRes.getId()).isPresent());
    }

    @Test
    @DisplayName("deleteCategory - 존재하지 않는 카테고리 삭제시 throw")
    public void deleteCategoryNotExist() {
        Assertions.assertThrows(NotFoundException.class, () -> categoryService.deleteCategory(1L));
    }
}

