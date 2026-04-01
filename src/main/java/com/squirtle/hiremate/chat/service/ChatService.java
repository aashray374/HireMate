package com.squirtle.hiremate.chat.service;

import com.squirtle.hiremate.chat.entity.MessageType;
import com.squirtle.hiremate.chat.entity.Message;
import com.squirtle.hiremate.job.entity.Job;

import java.util.UUID;

public interface ChatService {
    Message saveMessage(UUID groupId, String senderEmail, String content, MessageType type, Job payload);
}
