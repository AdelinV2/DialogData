package com.dialogdata.main.mapper;

import com.dialogdata.main.dto.ReviewDto;
import com.dialogdata.main.entity.Review;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    Review toEntity(ReviewDto reviewDto);

    ReviewDto toDto(Review review);

    List<Review> toEntityList(List<ReviewDto> reviewDtos);

    List<ReviewDto> toDtoList(List<Review> reviews);
}
