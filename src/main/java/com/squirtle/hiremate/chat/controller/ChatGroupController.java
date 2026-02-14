package com.squirtle.hiremate.chat.controller;

import com.squirtle.hiremate.chat.entity.ChatGroup;
import com.squirtle.hiremate.chat.service.ChatGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
public class ChatGroupController {

    private final ChatGroupService chatGroupService;

    public ChatGroupController(ChatGroupService chatGroupService) {
        this.chatGroupService = chatGroupService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestParam String name,
                                         Principal principal) {

        ChatGroup group = chatGroupService
                .createGroup(name, principal.getName());

        return ResponseEntity.ok(group);
    }

    @PostMapping("/{groupId}/add-member")
    public ResponseEntity<?> addMember(@PathVariable UUID groupId,
                                       @RequestParam String email,
                                       Principal principal) {

        chatGroupService.addMember(
                groupId,
                principal.getName(),
                email
        );

        return ResponseEntity.ok("Member added successfully");
    }
}
