package com.dialogdata.backend.mapper;

import com.dialogdata.backend.dto.ProductAttributeDto;
import com.dialogdata.backend.entity.ProductAttribute;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductAttributeMapper {

    ProductAttribute toEntity(ProductAttributeDto productAttributeDto);

    ProductAttributeDto toDto(ProductAttribute productAttribute);

    List<ProductAttribute> toEntityList(List<ProductAttributeDto> productAttributeDtos);

    List<ProductAttributeDto> toDtoList(List<ProductAttribute> productAttributes);
}
