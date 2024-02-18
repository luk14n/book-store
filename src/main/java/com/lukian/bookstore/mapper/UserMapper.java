package com.lukian.bookstore.mapper;

import com.lukian.bookstore.config.MapperConfig;
import com.lukian.bookstore.dto.user.CreateUserRequestDto;
import com.lukian.bookstore.dto.user.UserDto;
import com.lukian.bookstore.model.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(config = MapperConfig.class)
@Component
public interface UserMapper {
    UserDto toDto(User user);

    User toModel(CreateUserRequestDto requestDto);
}
