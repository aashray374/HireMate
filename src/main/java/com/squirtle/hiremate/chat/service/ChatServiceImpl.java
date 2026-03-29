package com.squirtle.hiremate.chat.service;

import com.squirtle.hiremate.auth.service.EmailService;
import com.squirtle.hiremate.chat.dto.MessageType;
import com.squirtle.hiremate.chat.entity.*;
import com.squirtle.hiremate.chat.repository.*;
import com.squirtle.hiremate.config.DynamicEmailConfig;
import com.squirtle.hiremate.contacts.entity.Contact;
import com.squirtle.hiremate.contacts.repository.ContactRepository;
import com.squirtle.hiremate.exception.*;
import com.squirtle.hiremate.user.entity.User;
import com.squirtle.hiremate.user.repository.UserRepository;
import com.squirtle.hiremate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final MessageRepository messageRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ChatGroupRepository chatGroupRepository;
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private final UserService userService;
    private final DynamicEmailConfig emailConfig;
    private final EmailService emailService;

    @Override
    public Message saveMessage(UUID groupId, String senderEmail, String content, MessageType type) {

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!groupMemberRepository.existsByGroupIdAndUserId(groupId, sender.getId())) {
            throw new UnauthorizedException("You are not part of this group");
        }

        ChatGroup group = chatGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        Message message = messageRepository.save(
                Message.builder()
                        .group(group)
                        .sender(sender)
                        .content(content)
                        .type(type)
                        .build()
        );

        if (type == MessageType.TRIGGER_EVENT) {
            handleTriggerEvent(groupId, sender);
        }

        return message;
    }

    private void handleTriggerEvent(UUID groupId, User sender) {

        if (sender.getAppPassword() == null || sender.getAppPassword().isBlank()) {
            throw new BadRequestException("Connect Gmail before triggering event");
        }

        List<Contact> contacts = contactRepository.findByCompanyIgnoreCase("google");

        if (contacts.isEmpty()) {
            throw new ResourceNotFoundException("No contacts found");
        }

        try {
            String subject = "Referral Request";
            String baseBody = userService.generateEmail(sender.getEmail());

            JavaMailSender mailSender =
                    emailConfig.createMailSender(sender.getEmail(), sender.getAppPassword());

            for (Contact contact : contacts) {
                String body = baseBody.replace("<<name>>", contact.getName());

                emailService.sendReferralEmail(
                        contact.getEmail(),
                        subject,
                        body
                );
            }

        } catch (Exception e) {
            log.error("Trigger event failed", e);
            throw new BadRequestException("Failed to send emails");
        }
    }
}