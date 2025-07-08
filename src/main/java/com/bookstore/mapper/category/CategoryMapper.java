package com.bookstore.mapper.category;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.category.CategoryDto;
import com.bookstore.dto.category.CategoryResponseDto;
import com.bookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    void updateCategoryFromDto(CategoryDto requestDto, @MappingTarget Category category);
}
