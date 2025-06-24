package com.bookstore.mapper.user;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.user.UserRegistrationRequestDto;
import com.bookstore.dto.user.UserResponseDto;
import com.bookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto modelToResponse(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
