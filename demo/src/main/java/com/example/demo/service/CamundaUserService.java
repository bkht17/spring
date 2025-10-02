package com.example.demo.service;

import com.example.demo.dto.CamundaUserDto;

import java.util.List;

public interface CamundaUserService {
    CamundaUserDto getUserById(String id);
    List<CamundaUserDto> getAll();
    List<CamundaUserDto> searchUsers(String searchTerm);

    CamundaUserDto create(CamundaUserDto user);
    CamundaUserDto update(String id, CamundaUserDto user);
    void delete(String id);
}