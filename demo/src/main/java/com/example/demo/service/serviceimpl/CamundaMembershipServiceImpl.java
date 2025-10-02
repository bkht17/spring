package com.example.demo.service.serviceimpl;

import com.example.demo.service.CamundaMembershipService;
import com.example.demo.service.CamundaUserService;
import com.example.demo.service.CamundaGroupService;
import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CamundaMembershipServiceImpl implements CamundaMembershipService {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private CamundaUserService camundaUserService;

    @Autowired
    private CamundaGroupService camundaGroupService;

    @Override
    public void addUserToGroup(String userId, String groupId) {
        if (camundaUserService.getUserById(userId) == null) {
            throw new RuntimeException("User not found: " + userId);
        }
        if (camundaGroupService.getGroupById(groupId) == null) {
            throw new RuntimeException("Group not found: " + groupId);
        }

        identityService.createMembership(userId, groupId);
    }

    @Override
    public void removeUserFromGroup(String userId, String groupId) {
        identityService.deleteMembership(userId, groupId);
    }
}