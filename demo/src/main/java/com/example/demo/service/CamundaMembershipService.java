package com.example.demo.service;

public interface CamundaMembershipService {
    void addUserToGroup(String userId, String groupId);
    void removeUserFromGroup(String userId, String groupId);
}