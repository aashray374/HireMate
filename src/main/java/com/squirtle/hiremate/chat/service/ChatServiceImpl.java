package com.squirtle.hiremate.chat.service;

import com.squirtle.hiremate.chat.entity.ChatGroup;
import com.squirtle.hiremate.chat.entity.Message;
import com.squirtle.hiremate.chat.repository.ChatGroupRepository;
import com.squirtle.hiremate.chat.repository.GroupMemberRepository;
import com.squirtle.hiremate.chat.repository.MessageRepository;
import com.squirtle.hiremate.user.entity.User;
import com.squirtle.hiremate.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class ChatServiceImpl implements ChatService{

    private final MessageRepository messageRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ChatGroupRepository chatGroupRepository;
    private final UserRepository userRepository;

    public ChatServiceImpl(MessageRepository messageRepository, GroupMemberRepository groupMemberRepository, ChatGroupRepository chatGroupRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.chatGroupRepository = chatGroupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Message saveMessage(UUID groupId, UUID senderId, String content) {
        if (!groupMemberRepository
                .existsByGroupIdAndUserId(groupId, senderId)) {
            throw new RuntimeException("User not in group");
        }

        ChatGroup group = chatGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Message message = Message.builder()
                .group(group)
                .sender(sender)
                .content(content)
                .build();

        return messageRepository.save(message);
    }
}
