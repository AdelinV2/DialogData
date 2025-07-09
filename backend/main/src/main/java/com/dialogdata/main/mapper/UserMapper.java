package com.dialogdata.main.mapper;

import com.dialogdata.main.dto.UserDto;
import com.dialogdata.main.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDto userDto);
    UserDto toDto(User user);
}
