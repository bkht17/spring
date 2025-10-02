package com.example.demo.service;

import com.example.demo.dto.CamundaGroupDto;
import com.example.demo.dto.CamundaUserDto;

import java.util.List;

public interface CamundaGroupService {
    CamundaGroupDto getGroupById(String id);
    List<CamundaGroupDto> getAll();

    CamundaGroupDto create(CamundaGroupDto group);
    CamundaGroupDto update(String id, CamundaGroupDto group);
    void delete(String id);

    List<CamundaGroupDto> getUserGroups(String userId);
    List<CamundaUserDto> getGroupUsers(String groupId);
}