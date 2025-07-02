package com.dialogdata.backend.mapper;

import com.dialogdata.backend.dto.UserDto;
import com.dialogdata.backend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDto userDto);
    UserDto toDto(User user);
}
