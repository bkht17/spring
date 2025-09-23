package com.example.demo.dto.mapping.mappingimpl;

import com.example.demo.dto.mapping.UserMapping;
import com.example.demo.model.User;
import com.example.demo.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMappingImpl implements UserMapping {


    @Override
    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.id());
        userDto.setFirstname(user.firstname());
        userDto.setLastname(user.lastname());
        userDto.setAge(user.age());

        return userDto;
    }

    @Override
    public List<UserDto> toUserDtos(List<User> users) {
        return users.stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }
}
