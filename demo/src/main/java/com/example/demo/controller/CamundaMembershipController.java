package com.example.demo.controller;

import com.example.demo.dto.MembershipDto;
import com.example.demo.service.CamundaMembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/camunda/memberships")
public class CamundaMembershipController {

    @Autowired
    private CamundaMembershipService camundaMembershipService;

    @PostMapping("")
    public ResponseEntity<Void> addUserToGroup(@RequestBody MembershipDto membershipDto) {
        try {
            camundaMembershipService.addUserToGroup(membershipDto.getUserId(), membershipDto.getGroupId());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Void> removeUserFromGroup(@RequestBody MembershipDto membershipDto) {
        try {
            camundaMembershipService.removeUserFromGroup(membershipDto.getUserId(), membershipDto.getGroupId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}