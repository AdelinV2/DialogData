package com.dialogdata.main.mapper;

import com.dialogdata.main.dto.ProductDto;
import com.dialogdata.main.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);

    List<ProductDto> toDtoList(List<Product> products);

    List<Product> toEntityList(List<ProductDto> productDtos);

}
