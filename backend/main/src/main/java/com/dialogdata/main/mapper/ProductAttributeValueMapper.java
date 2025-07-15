package com.dialogdata.main.mapper;

import com.dialogdata.main.dto.ProductAttributeValueDto;
import com.dialogdata.main.entity.ProductAttributeValue;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductAttributeValueMapper {

    ProductAttributeValue toEntity(ProductAttributeValueDto dto);

    ProductAttributeValueDto toDto(ProductAttributeValue entity);

    List<ProductAttributeValueDto> toDtoList(List<ProductAttributeValue> attributes);

    List<ProductAttributeValue> toEntityList(List<ProductAttributeValueDto> dtos);
}
