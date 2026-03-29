package com.squirtle.hiremate.chat.controller;

import com.squirtle.hiremate.chat.dto.ChatMessageDto;
import com.squirtle.hiremate.chat.entity.Message;
import com.squirtle.hiremate.chat.service.ChatService;
import com.squirtle.hiremate.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/group/{groupId}")
    public void sendMessage(
            @DestinationVariable UUID groupId,
            ChatMessageDto dto,
            Principal principal
    ) {

        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new BadRequestException("Message cannot be empty");
        }

        Message saved = chatService.saveMessage(
                groupId,
                principal.getName(),
                dto.getContent(),
                dto.getType()
        );

        messagingTemplate.convertAndSend(
                "/topic/group/" + groupId,
                saved
        );
    }
}