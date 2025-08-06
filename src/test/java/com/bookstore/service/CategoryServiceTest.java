package com.bookstore.service;

import static com.bookstore.util.TestUtil.createListOfTwoCategories;
import static com.bookstore.util.TestUtil.createListOfTwoCategoriesDtos;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bookstore.dto.category.CategoryDto;
import com.bookstore.dto.category.CategoryResponseDto;
import com.bookstore.mapper.category.CategoryMapper;
import com.bookstore.model.Category;
import com.bookstore.repository.category.CategoryRepository;
import com.bookstore.service.category.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
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
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("findAll should return all categories")
    void findAll_TwoCategories_Ok() {
        List<Category> categories = createListOfTwoCategories();
        List<CategoryResponseDto> categoryResponseDtos = createListOfTwoCategoriesDtos();
        Pageable pageable = PageRequest.of(0,10);
        Page<Category> pageCategories = new PageImpl<>(categories,pageable,2);

        when(categoryRepository.findAll(pageable)).thenReturn(pageCategories);
        when(categoryMapper.toDto(categories.get(0))).thenReturn(categoryResponseDtos.get(0));
        when(categoryMapper.toDto(categories.get(1))).thenReturn(categoryResponseDtos.get(1));
        Page<CategoryResponseDto> actual = categoryService.findAll(pageable);

        Page<CategoryResponseDto> expected = new PageImpl<>(categoryResponseDtos,pageable,2);

        assertEquals(expected.getContent(),actual.getContent());

        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(categories.get(0));
        verify(categoryMapper).toDto(categories.get(1));
    }

    @Test
    @DisplayName("Should return category by valid id")
    void getCategoryById_WithValidId_ok() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Horror");
        CategoryResponseDto categoryDto = new CategoryResponseDto();
        categoryDto.setId(categoryId);
        categoryDto.setName("Horror");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryResponseDto actual = categoryService.getById(categoryId);

        assertEquals(actual,categoryDto);
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Should create and save new category")
    void saveCategory_WithValidRequest_Ok() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Horror");

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(categoryId);
        categoryResponseDto.setName("Horror");

        CategoryDto requestDto = new CategoryDto();
        requestDto.setName("Horror");

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto actual = categoryService.save(requestDto);

        assertEquals(actual,categoryResponseDto);

        verify(categoryMapper).toEntity(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("update should return correct category dto")
    void updateCategory_RequestToChangeName_OK() {
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Horror");

        Category changedCategory = new Category();
        changedCategory.setId(categoryId);
        changedCategory.setName("Comedy");

        CategoryDto requestDto = new CategoryDto();
        requestDto.setName("Comedy");

        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(categoryId);
        responseDto.setName("Comedy");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateCategoryFromDto(requestDto, category);
        when(categoryRepository.save(category)).thenReturn(changedCategory);
        when(categoryMapper.toDto(changedCategory)).thenReturn(responseDto);

        CategoryResponseDto actual = categoryService.update(categoryId,requestDto);

        assertEquals(actual,responseDto);
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).updateCategoryFromDto(requestDto, category);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(changedCategory);
    }

    @Test
    @DisplayName("Delete should remove category by id")
    void deleteCategory_WithValidId_Ok() {
        Long categoryId = 1L;

        categoryService.deleteById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }
}
