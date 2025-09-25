package com.example.demo.service;


import com.example.demo.dto.UserFilter;
import com.example.demo.model.User;
import com.example.demo.dto.UserDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface UserService {
    UserDto getUserById(Long id);
    List<UserDto> getAll();
    List<UserDto> getAll(UserFilter userFilter);

    User create(User user);
    User update(Long id, User user);
    void delete(Long id);

    void saveFileData(InputStream file, Long id) throws IOException;

}
