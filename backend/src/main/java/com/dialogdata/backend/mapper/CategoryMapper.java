package com.dialogdata.backend.mapper;

import com.dialogdata.backend.dto.CategoryDto;
import com.dialogdata.backend.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryDto categoryDto);
    CategoryDto toDto(Category category);
    List<Category> toEntityList(List<CategoryDto> categoryDtos);
    List<CategoryDto> toDtoList(List<Category> categories);
}
