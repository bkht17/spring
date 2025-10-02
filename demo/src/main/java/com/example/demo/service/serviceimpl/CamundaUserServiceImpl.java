package com.example.demo.service.serviceimpl;

import com.example.demo.dto.CamundaUserDto;
import com.example.demo.service.CamundaUserService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CamundaUserServiceImpl implements CamundaUserService {

    @Autowired
    private IdentityService identityService;

    @Override
    public CamundaUserDto getUserById(String id) {
        User user = identityService.createUserQuery()
                .userId(id)
                .singleResult();

        return user != null ? toUserDto(user) : null;
    }

    @Override
    public List<CamundaUserDto> getAll() {
        return identityService.createUserQuery()
                .orderByUserId()
                .asc()
                .list()
                .stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CamundaUserDto> searchUsers(String searchTerm) {
        List<User> allUsers = identityService.createUserQuery()
                .orderByUserId()
                .asc()
                .list();

        return allUsers.stream()
                .filter(user -> matchesSearch(user, searchTerm))
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public CamundaUserDto create(CamundaUserDto userDto) {
        User existingUser = identityService.createUserQuery()
                .userId(userDto.getId())
                .singleResult();

        if (existingUser != null) {
            throw new RuntimeException("User already exists: " + userDto.getId());
        }

        User user = identityService.newUser(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        identityService.saveUser(user);

        return toUserDto(user);
    }

    @Override
    public CamundaUserDto update(String id, CamundaUserDto userDto) {
        User user = identityService.createUserQuery()
                .userId(id)
                .singleResult();

        if (user == null) {
            throw new RuntimeException("User not found: " + id);
        }

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(userDto.getPassword());
        }

        identityService.saveUser(user);

        return toUserDto(user);
    }

    @Override
    public void delete(String id) {
        identityService.deleteUser(id);
    }

    private CamundaUserDto toUserDto(User user) {
        CamundaUserDto dto = new CamundaUserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    private boolean matchesSearch(User user, String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return true;
        }

        String lowerSearch = searchTerm.toLowerCase();
        return (user.getId() != null && user.getId().toLowerCase().contains(lowerSearch)) ||
                (user.getFirstName() != null && user.getFirstName().toLowerCase().contains(lowerSearch)) ||
                (user.getLastName() != null && user.getLastName().toLowerCase().contains(lowerSearch)) ||
                (user.getEmail() != null && user.getEmail().toLowerCase().contains(lowerSearch));
    }
}