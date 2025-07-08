package com.bookstore.service.category;

import com.bookstore.dto.category.CategoryDto;
import com.bookstore.dto.category.CategoryResponseDto;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.mapper.category.CategoryMapper;
import com.bookstore.model.Category;
import com.bookstore.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryResponseDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found by id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto save(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryResponseDto update(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found by id " + id)
                );
        categoryMapper.updateCategoryFromDto(categoryDto, category);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
