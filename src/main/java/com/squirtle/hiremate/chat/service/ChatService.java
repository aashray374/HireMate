package com.squirtle.hiremate.chat.service;

import com.squirtle.hiremate.chat.entity.Message;

import java.util.UUID;

public interface ChatService {
    Message saveMessage(UUID groupId, UUID senderId, String content);
}
