package com.squirtle.hiremate.chat.service;

import com.squirtle.hiremate.common.config.RabbitMQConfig;
import com.squirtle.hiremate.common.email.dto.ReferralEmailMessage;
import com.squirtle.hiremate.chat.dto.MessageType;
import com.squirtle.hiremate.chat.entity.*;
import com.squirtle.hiremate.chat.repository.*;
import com.squirtle.hiremate.common.exception.BadRequestException;
import com.squirtle.hiremate.common.exception.ResourceNotFoundException;
import com.squirtle.hiremate.common.exception.UnauthorizedException;
import com.squirtle.hiremate.common.config.DynamicEmailConfig;
import com.squirtle.hiremate.common.utils.CloudinaryUtil;
import com.squirtle.hiremate.contacts.entity.Contact;
import com.squirtle.hiremate.contacts.repository.ContactRepository;
import com.squirtle.hiremate.user.entity.User;
import com.squirtle.hiremate.user.repository.UserRepository;
import com.squirtle.hiremate.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private final MessageRepository messageRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ChatGroupRepository chatGroupRepository;
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private final UserService userService;
    private final DynamicEmailConfig emailConfig;
    private final CloudinaryUtil cloudinaryUtil;
    private final RabbitTemplate rabbitTemplate;

    public ChatServiceImpl(MessageRepository messageRepository, GroupMemberRepository groupMemberRepository, ChatGroupRepository chatGroupRepository, UserRepository userRepository, ContactRepository contactRepository, UserService userService, DynamicEmailConfig emailConfig, CloudinaryUtil cloudinaryUtil, RabbitTemplate rabbitTemplate) {
        this.messageRepository = messageRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.chatGroupRepository = chatGroupRepository;
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.userService = userService;
        this.emailConfig = emailConfig;
        this.cloudinaryUtil = cloudinaryUtil;
        this.rabbitTemplate = rabbitTemplate;
    }

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
            handleTriggerEvent(groupId);
        }

        return message;
    }

    private void handleTriggerEvent(UUID groupId) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EVENT_EXCHANGE,RabbitMQConfig.EVENT_ROUTING_KEY,groupId);
    }


    @RabbitListener(queues = RabbitMQConfig.EVENT_QUEUE)
    public void consumeTriggerEvent(UUID groupId){
        String company = "";
        List<Contact> contacts = contactRepository.findByCompanyIgnoreCase(company);

        try {
            String subject = "Referral Request";
            List<GroupMember> members  = groupMemberRepository.findByGroupId(groupId);

            for(GroupMember m : members){
                User user = m.getUser();
                String baseBody = userService.generateEmail(user.getEmail());
                String fileName = user.getUsername()+"_resume.pdf";
                byte[] file = cloudinaryUtil.downloadFile(user.getResume().getUrl());
                JavaMailSender mailSender = emailConfig.createMailSender(user.getEmail(), user.getAppPassword());

                for (Contact contact : contacts) {
                    String body = baseBody.replace("<<name>>", contact.getName());

                    ReferralEmailMessage msg = ReferralEmailMessage.builder()
                            .to(contact.getEmail())
                            .body(body)
                            .subject(subject)
                            .email(user.getEmail())
                            .password(user.getAppPassword())
                            .fileName(fileName)
                            .file(file)
                            .build();

                    rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE,RabbitMQConfig.EVENT_ROUTING_KEY,msg);
                }
            }

        } catch (Exception e) {
            log.error("Trigger event failed", e);
            throw new BadRequestException("Failed to send emails");
        }

    }
}