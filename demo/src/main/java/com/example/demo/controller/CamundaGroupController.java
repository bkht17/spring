package com.example.demo.controller;

import com.example.demo.dto.CamundaGroupDto;
import com.example.demo.dto.CamundaUserDto;
import com.example.demo.service.CamundaGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/camunda/groups")
public class CamundaGroupController {

    @Autowired
    private CamundaGroupService camundaGroupService;

    @GetMapping("/{id}")
    public ResponseEntity<CamundaGroupDto> get(@PathVariable("id") String id) {
        CamundaGroupDto group = camundaGroupService.getGroupById(id);
        if (group != null) {
            return ResponseEntity.ok(group);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<CamundaGroupDto>> getAll() {
        return ResponseEntity.ok(camundaGroupService.getAll());
    }

    @PostMapping("")
    public ResponseEntity<CamundaGroupDto> create(@RequestBody CamundaGroupDto group) {
        try {
            CamundaGroupDto createdGroup = camundaGroupService.create(group);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CamundaGroupDto> update(@PathVariable("id") String id, @RequestBody CamundaGroupDto group) {
        try {
            CamundaGroupDto updatedGroup = camundaGroupService.update(id, group);
            return ResponseEntity.ok(updatedGroup);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        try {
            camundaGroupService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{groupId}/users")
    public ResponseEntity<List<CamundaUserDto>> getGroupUsers(@PathVariable String groupId) {
        try {
            List<CamundaUserDto> users = camundaGroupService.getGroupUsers(groupId);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CamundaGroupDto>> getUserGroups(@PathVariable String userId) {
        try {
            List<CamundaGroupDto> groups = camundaGroupService.getUserGroups(userId);
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}