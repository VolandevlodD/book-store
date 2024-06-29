package org.example.bookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CreateCategoryRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.CategoryMapper;
import org.example.bookstore.model.Category;
import org.example.bookstore.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    private static final Long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "Category Name";
    private static final String CATEGORY_DESCRIPTION = "Category Description";
    private static final String UPDATED_CATEGORY_NAME = "Updated Category Name";
    private static final String UPDATED_CATEGORY_DESCRIPTION = "Updated Category Description";
    private static final Set<Long> CATEGORY_IDS = Set.of(CATEGORY_ID);

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CreateCategoryRequestDto createCategoryRequestDto;
    private Category category;

    @BeforeEach
    void setUp() {
        createCategoryRequestDto =
                new CreateCategoryRequestDto(CATEGORY_NAME, CATEGORY_DESCRIPTION);
        category = createCategory();
    }

    @Test
    @DisplayName("Save valid category")
    void save_ValidCategory_ReturnsValidCategoryDto() {
        CategoryDto expected = new CategoryDto(CATEGORY_ID, CATEGORY_NAME, CATEGORY_DESCRIPTION);
        when(categoryMapper.toEntity(any(CreateCategoryRequestDto.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(expected);

        CategoryDto actual = categoryService.save(createCategoryRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get existing category by ID")
    void getById_ExistingId_ReturnsCategoryDto() {
        CategoryDto expected = new CategoryDto(CATEGORY_ID, CATEGORY_NAME, CATEGORY_DESCRIPTION);
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(any(Category.class))).thenReturn(expected);

        CategoryDto actual = categoryService.getById(CATEGORY_ID);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get non-existing category by ID")
    void getById_NonExistingId_ThrowsException() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(CATEGORY_ID));

        assertEquals("Category with id %d not found!".formatted(CATEGORY_ID), actual.getMessage());
    }

    @Test
    @DisplayName("Find all categories with default pageable params")
    void findAll_DefaultPageableParams_ReturnsAllCategoryDtos() {
        Page<Category> categoryPage = new PageImpl<>(List.of(category, category));
        Pageable defaultPageable = PageRequest.of(0, 20);
        when(categoryRepository.findAll(defaultPageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(toCategoryDto(category));

        List<CategoryDto> response = categoryService.findAll(defaultPageable);

        assertEquals(categoryPage.getSize(), response.size());
        verify(categoryRepository).findAll(any(Pageable.class));
        verify(categoryMapper, times(categoryPage.getSize())).toDto(any(Category.class));
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Update category with valid data")
    void update_ValidData_Success() {
        Category updatedCategory = new Category();
        updatedCategory.setId(CATEGORY_ID);
        updatedCategory.setName(UPDATED_CATEGORY_NAME);
        updatedCategory.setDescription(UPDATED_CATEGORY_DESCRIPTION);

        CategoryDto expected =
                new CategoryDto(CATEGORY_ID, UPDATED_CATEGORY_NAME, UPDATED_CATEGORY_DESCRIPTION);

        when(categoryRepository.existsById(CATEGORY_ID)).thenReturn(true);
        when(categoryMapper.toEntity(any(CreateCategoryRequestDto.class))).thenReturn(
                updatedCategory);
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(expected);

        CategoryDto actual = categoryService.update(CATEGORY_ID,
                new CreateCategoryRequestDto(UPDATED_CATEGORY_NAME, UPDATED_CATEGORY_DESCRIPTION));

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update category with invalid ID")
    void update_InvalidId_ThrowsException() {
        when(categoryRepository.existsById(CATEGORY_ID)).thenReturn(false);

        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(CATEGORY_ID, createCategoryRequestDto));

        assertEquals("Category with id %d not found".formatted(CATEGORY_ID), actual.getMessage());
    }

    @Test
    @DisplayName("Delete category by valid ID")
    void deleteById_ValidId_Success() {
        when(categoryRepository.existsById(CATEGORY_ID)).thenReturn(true);

        categoryService.deleteById(CATEGORY_ID);

        verify(categoryRepository).deleteById(CATEGORY_ID);
    }

    @Test
    @DisplayName("Delete category by invalid ID")
    void deleteById_InvalidId_ThrowsException() {
        when(categoryRepository.existsById(CATEGORY_ID)).thenReturn(false);

        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> categoryService.deleteById(CATEGORY_ID));

        assertEquals("Category with id %d not found".formatted(CATEGORY_ID), actual.getMessage());
    }

    @Test
    @DisplayName("Check if categories exist by IDs")
    void checkIfCategoriesExistsByIds_ValidIds_Success() {
        when(categoryRepository.findExistingIdsByIdIn(CATEGORY_IDS)).thenReturn(CATEGORY_IDS);

        categoryService.checkIfCategoriesExistsByIds(CATEGORY_IDS);

        verify(categoryRepository).findExistingIdsByIdIn(CATEGORY_IDS);
    }

    @Test
    @DisplayName("Check if categories exist by IDs with missing IDs")
    void checkIfCategoriesExistsByIds_InvalidIds_ThrowsException() {
        when(categoryRepository.findExistingIdsByIdIn(CATEGORY_IDS)).thenReturn(Set.of());

        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> categoryService.checkIfCategoriesExistsByIds(CATEGORY_IDS));

        assertEquals("Categories with ID's " + CATEGORY_IDS + " not found", actual.getMessage());
    }

    private Category createCategory() {
        Category category = new Category();
        category.setId(CATEGORY_ID);
        category.setName(CATEGORY_NAME);
        category.setDescription(CATEGORY_DESCRIPTION);
        return category;
    }

    private CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName(), category.getDescription());
    }
}
