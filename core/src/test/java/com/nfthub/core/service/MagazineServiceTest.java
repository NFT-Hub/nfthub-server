package com.nfthub.core.service;

import com.nfthub.core.entity.Category;
import com.nfthub.core.entity.Magazine;
import com.nfthub.core.entity.Tag;
import com.nfthub.core.exception.NotFoundException;
import com.nfthub.core.repository.CategoryRepository;
import com.nfthub.core.repository.MagazineRepository;
import com.nfthub.core.repository.TagRepository;
import com.nfthub.core.request.MagazineRequest;
import com.nfthub.core.response.MagazineResponse;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MagazineServiceTest {
    List<Category> categories;
    List<Tag> tags;
    @Autowired
    private MagazineService magazineService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private MagazineRepository magazineRepository;
    // mock s3service
    @MockBean
    private S3Service s3Service;

    @BeforeAll
    public void setup() {
        Category category1 = new Category();
        category1.setName("category1");
        Category category2 = new Category();
        category2.setName("category2");
        categories = List.of(category1, category2);
        categoryRepository.saveAll(categories);

        Tag tag1 = new Tag();
        tag1.setName("tag1");
        Tag tag2 = new Tag();
        tag2.setName("tag2");
        tags = List.of(tag1, tag2);
        tagRepository.saveAll(tags);

        Mockito.when(s3Service.uploadImage(Mockito.any(), Mockito.any())).thenReturn("https://s3.amazonaws.com/nfthub/magazine/1");
        Mockito.doNothing().when(s3Service).deleteImage(Mockito.any());

    }

    @Test
    @DisplayName("getMagazineResOrThrow - 매거진 조회")
    public void getMagazineResOrThrow() {
        MagazineRequest request = MagazineRequest.builder()
                .title("title")
                .description("description")
                .url("url")
                .author("author")
                .build();
        MagazineResponse created = magazineService.createMagazine(request);
        MagazineResponse magazineRes = magazineService.getMagazineResOrThrow(created.getId());
        Assertions.assertEquals(created.getId(), magazineRes.getId());
    }

    @Test
    @DisplayName("getMagazineResOrThrow - 없으면 throw")
    public void getMagazineResOrThrow_throw() {
        Assertions.assertThrows(
                NotFoundException.class,
                () -> magazineService.getMagazineResOrThrow(1L)
        );
    }

    @Test
    @DisplayName("createMagazine - 생성되는지 확인")
    public void createMagazine() {
        MagazineRequest request = MagazineRequest.builder()
                .title("title")
                .description("description")
                .url("url")
                .author("author")
                .build();
        MagazineResponse created = magazineService.createMagazine(request);
        Assertions.assertDoesNotThrow(() -> {
            Magazine magazine = magazineRepository.findById(created.getId()).orElseThrow();
            Assertions.assertEquals(created.getId(), magazine.getId());
        });
    }

    @Test
    @DisplayName("createMagazine - 카테고리 또는 태그가 없으면 throw")
    public void createMagazine_throw() {
        MagazineRequest request = MagazineRequest.builder()
                .title("title")
                .description("description")
                .url("url")
                .author("author")
                .categoryId(categories.get(0).getId())
                .tagIds(List.of(tags.get(0).getId()))
                .build();
        Assertions.assertDoesNotThrow(
                () -> magazineService.createMagazine(request)
        );

        MagazineRequest request2 = MagazineRequest.builder()
                .title("title")
                .description("description")
                .url("url")
                .author("author")
                .categoryId(categories.get(0).getId() + 100)
                .tagIds(List.of(tags.get(0).getId()))
                .build();
        Assertions.assertThrows(
                NotFoundException.class,
                () -> magazineService.createMagazine(request2)
        );

        MagazineRequest request3 = MagazineRequest.builder()
                .title("title")
                .description("description")
                .url("url")
                .author("author")
                .categoryId(categories.get(0).getId())
                .tagIds(List.of(tags.get(0).getId() + 100))
                .build();
        Assertions.assertThrows(
                NotFoundException.class,
                () -> magazineService.createMagazine(request3)
        );
    }

    @Test
    @DisplayName("updateMagazine - 수정되는지 확인")
    @Transactional
    public void updateMagazine() {
        MagazineRequest request = MagazineRequest.builder()
                .title("title")
                .description("description")
                .url("url")
                .author("author")
                .categoryId(categories.get(0).getId())
                .tagIds(
                        List.of(tags.get(0).getId())
                )
                .build();
        MagazineResponse created = magazineService.createMagazine(request);
        MagazineRequest updateRequest = MagazineRequest.builder()
                .title("title2")
                .description("description2")
                .url("url2")
                .author("author2")
                .categoryId(categories.get(1).getId())
                .tagIds(
                        List.of(tags.get(1).getId())
                )
                .build();
        MagazineResponse updated = magazineService.updateMagazine(created.getId(), updateRequest);
        Assertions.assertDoesNotThrow(() -> {
            Magazine magazine = magazineRepository.findById(updated.getId()).orElseThrow();
            Assertions.assertEquals(updated.getId(), magazine.getId());
            Assertions.assertEquals(updateRequest.getTitle(), magazine.getTitle());
            Assertions.assertEquals(updateRequest.getDescription(), magazine.getDescription());
            Assertions.assertEquals(updateRequest.getUrl(), magazine.getUrl());
            Assertions.assertEquals(updateRequest.getAuthor(), magazine.getAuthor());
            Assertions.assertEquals(updateRequest.getCategoryId(), magazine.getCategory().getId());
            Assertions.assertEquals(updateRequest.getTagIds().get(0), magazine.getMagazineTags().get(0).getTag().getId());
            Assertions.assertEquals(1, magazine.getMagazineTags().size());
        });
    }

    @Test
    @DisplayName("deleteMagazine - 삭제되는지 확인")
    public void deleteMagazine() {
        MagazineRequest request = MagazineRequest.builder()
                .title("title")
                .description("description")
                .url("url")
                .author("author")
                .build();
        MagazineResponse created = magazineService.createMagazine(request);
        magazineService.deleteMagazine(created.getId());
        Assertions.assertThrows(
                NotFoundException.class,
                () -> magazineService.getMagazineResOrThrow(created.getId())
        );
    }

    @Test
    @DisplayName("createMagazineImages - 이미지 생성되는지 확인")
    @Transactional
    public void createMagazineImages() {
        // mock multipart
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());
        // mock magazine
        MagazineRequest request = MagazineRequest.builder()
                .title("title")
                .description("description")
                .url("url")
                .author("author")
                .build();
        MagazineResponse created = magazineService.createMagazine(request);

        magazineService.createMagazineImages(created.getId(), List.of(file));
        Assertions.assertDoesNotThrow(() -> {
            Magazine magazine = magazineRepository.findById(created.getId()).orElseThrow();
            Assertions.assertEquals(1, magazine.getMagazineImages().size());
        });
    }

    @Test
    @DisplayName("updateMagazineImage - 이미지가 수정되는지 확인")
    public void updateMagazineImage() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());
        MagazineRequest request = MagazineRequest.builder()
                .title("title")
                .description("description")
                .url("url")
                .author("author")
                .build();
        MagazineResponse created = magazineService.createMagazine(request);
        MagazineResponse createdWithImage = magazineService.createMagazineImages(created.getId(), List.of(file));

        MockMultipartFile file2 = new MockMultipartFile("file2", "test2.jpg", "image/jpeg", "test2".getBytes());
        MagazineResponse updatedWithImage = magazineService.updateMagazineImage(createdWithImage.getId(), createdWithImage.getImages().get(0).getId(), file2);
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals(createdWithImage.getImages().get(0).getId(), updatedWithImage.getImages().get(0).getId());
        });
    }

    @Test
    @DisplayName("updateMagazineImage - 이미지가 없으면 throw")
    public void updateMagazineImage_throw() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());
        MagazineRequest request = MagazineRequest.builder()
                .title("title")
                .description("description")
                .url("url")
                .author("author")
                .build();
        MagazineResponse created = magazineService.createMagazine(request);

        MagazineResponse createdWithImage = magazineService.createMagazineImages(created.getId(), List.of(file));

        MockMultipartFile file2 = new MockMultipartFile("file", "test2.jpg", "image/jpeg", "test2".getBytes());
        Assertions.assertThrows(
                NotFoundException.class,
                () -> magazineService.updateMagazineImage(createdWithImage.getId(), createdWithImage.getImages().get(0).getId() + 100, file2)
        );
    }

    @Test
    @DisplayName("deleteMagazineImages - 이미지 삭제되는지 확인")
    @Transactional
    public void deleteMagazineImages() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());
        MagazineRequest request = MagazineRequest.builder()
                .title("title")
                .description("description")
                .url("url")
                .author("author")
                .build();
        MagazineResponse created = magazineService.createMagazine(request);

        MagazineResponse createdWithImage = magazineService.createMagazineImages(created.getId(), List.of(file));

        magazineService.deleteMagazineImage(createdWithImage.getId(), createdWithImage.getImages().get(0).getId());

        Magazine magazine = magazineRepository.findById(createdWithImage.getId()).orElseThrow();
        Assertions.assertEquals(0, magazine.getMagazineImages().size());
    }
}
