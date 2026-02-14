package com.squirtle.hiremate.chat.controller;

import com.squirtle.hiremate.chat.dto.ChatMessageDto;
import com.squirtle.hiremate.chat.entity.Message;
import com.squirtle.hiremate.chat.service.ChatService;
import com.squirtle.hiremate.user.service.UserService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatService chatService, UserService userService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/group/{groupId}")
    public void sendMessage(@DestinationVariable UUID groupId,
                            ChatMessageDto dto,
                            Principal principal) {

        String email = principal.getName();

        UUID senderId = userService
                .getUserByEmail(email)
                .getId();

        Message saved = chatService.saveMessage(
                groupId,
                senderId,
                dto.getContent()
        );

        messagingTemplate.convertAndSend(
                "/topic/group/" + groupId,
                saved
        );
    }
}
