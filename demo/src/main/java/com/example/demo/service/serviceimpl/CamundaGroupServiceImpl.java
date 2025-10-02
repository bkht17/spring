package com.example.demo.service.serviceimpl;

import com.example.demo.dto.CamundaGroupDto;
import com.example.demo.dto.CamundaUserDto;
import com.example.demo.service.CamundaGroupService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CamundaGroupServiceImpl implements CamundaGroupService {

    @Autowired
    private IdentityService identityService;

    @Override
    public CamundaGroupDto getGroupById(String id) {
        Group group = identityService.createGroupQuery()
                .groupId(id)
                .singleResult();

        return group != null ? toGroupDto(group) : null;
    }

    @Override
    public List<CamundaGroupDto> getAll() {
        return identityService.createGroupQuery()
                .orderByGroupId()
                .asc()
                .list()
                .stream()
                .map(this::toGroupDto)
                .collect(Collectors.toList());
    }

    @Override
    public CamundaGroupDto create(CamundaGroupDto groupDto) {
        Group existingGroup = identityService.createGroupQuery()
                .groupId(groupDto.getId())
                .singleResult();

        if (existingGroup != null) {
            throw new RuntimeException("Group already exists: " + groupDto.getId());
        }

        Group group = identityService.newGroup(groupDto.getId());
        group.setName(groupDto.getName());
        group.setType(groupDto.getType());

        identityService.saveGroup(group);

        return toGroupDto(group);
    }

    @Override
    public CamundaGroupDto update(String id, CamundaGroupDto groupDto) {
        Group group = identityService.createGroupQuery()
                .groupId(id)
                .singleResult();

        if (group == null) {
            throw new RuntimeException("Group not found: " + id);
        }

        group.setName(groupDto.getName());
        group.setType(groupDto.getType());

        identityService.saveGroup(group);

        return toGroupDto(group);
    }

    @Override
    public void delete(String id) {
        identityService.deleteGroup(id);
    }

    @Override
    public List<CamundaGroupDto> getUserGroups(String userId) {
        return identityService.createGroupQuery()
                .groupMember(userId)
                .list()
                .stream()
                .map(this::toGroupDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CamundaUserDto> getGroupUsers(String groupId) {
        return identityService.createUserQuery()
                .memberOfGroup(groupId)
                .list()
                .stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }

    private CamundaGroupDto toGroupDto(Group group) {
        CamundaGroupDto dto = new CamundaGroupDto();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setType(group.getType());
        return dto;
    }

    private CamundaUserDto toUserDto(User user) {
        CamundaUserDto dto = new CamundaUserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}