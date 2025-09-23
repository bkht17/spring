package com.example.demo.dto.mapping;

import com.example.demo.model.User;
import com.example.demo.dto.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapping {
    UserDto toUserDto(User user);
    List<UserDto> toUserDtos(List<User> users);
}
