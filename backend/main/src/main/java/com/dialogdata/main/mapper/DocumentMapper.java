package com.dialogdata.main.mapper;

import com.dialogdata.main.dto.DocumentDto;
import com.dialogdata.main.entity.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    Document toEntity(DocumentDto documentDto);

    DocumentDto toDto(Document document);
}
