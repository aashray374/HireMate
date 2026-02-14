package com.squirtle.hiremate.chat.service;

import com.squirtle.hiremate.chat.entity.ChatGroup;

import java.util.UUID;

public interface ChatGroupService {

    ChatGroup createGroup(String groupName, String creatorEmail);
    void addMember(UUID groupId, String requestingUserEmail, String newMemberEmail);
}
