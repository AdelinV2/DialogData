package com.dialogdata.main.mapper;

import com.dialogdata.main.dto.NewsletterDto;
import com.dialogdata.main.entity.Newsletter;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NewsletterMapper {

    Newsletter toEntity(NewsletterDto newsletterDto);

    NewsletterDto toDto(Newsletter newsletter);

    List<Newsletter> toEntityList(List<NewsletterDto> newsletterDtos);

    List<NewsletterDto> toDtoList(List<Newsletter> newsletters);
}
