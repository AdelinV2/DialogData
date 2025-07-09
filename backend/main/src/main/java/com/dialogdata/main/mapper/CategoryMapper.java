package com.dialogdata.main.mapper;

import com.dialogdata.main.dto.CategoryDto;
import com.dialogdata.main.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryDto categoryDto);
    CategoryDto toDto(Category category);
    List<Category> toEntityList(List<CategoryDto> categoryDtos);
    List<CategoryDto> toDtoList(List<Category> categories);
}
