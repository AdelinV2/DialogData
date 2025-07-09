package com.dialogdata.main.mapper;

import com.dialogdata.main.dto.ProductAttributeDto;
import com.dialogdata.main.entity.ProductAttribute;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductAttributeMapper {

    ProductAttribute toEntity(ProductAttributeDto productAttributeDto);

    ProductAttributeDto toDto(ProductAttribute productAttribute);

    List<ProductAttribute> toEntityList(List<ProductAttributeDto> productAttributeDtos);

    List<ProductAttributeDto> toDtoList(List<ProductAttribute> productAttributes);
}
