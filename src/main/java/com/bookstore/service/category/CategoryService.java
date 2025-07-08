package com.bookstore.service.category;

import com.bookstore.dto.category.CategoryDto;
import com.bookstore.dto.category.CategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryDto categoryDto);

    CategoryResponseDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);
}
