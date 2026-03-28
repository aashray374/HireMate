package com.squirtle.hiremate.chat.service;

import com.squirtle.hiremate.auth.service.EmailService;
import com.squirtle.hiremate.chat.dto.MessageType;
import com.squirtle.hiremate.chat.entity.ChatGroup;
import com.squirtle.hiremate.chat.entity.GroupMember;
import com.squirtle.hiremate.chat.entity.Message;
import com.squirtle.hiremate.chat.repository.ChatGroupRepository;
import com.squirtle.hiremate.chat.repository.GroupMemberRepository;
import com.squirtle.hiremate.chat.repository.MessageRepository;
import com.squirtle.hiremate.config.DynamicEmailConfig;
import com.squirtle.hiremate.contacts.entity.Contact;
import com.squirtle.hiremate.contacts.repository.ContactRepository;
import com.squirtle.hiremate.user.entity.User;
import com.squirtle.hiremate.user.repository.UserRepository;
import com.squirtle.hiremate.user.service.UserService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class ChatServiceImpl implements ChatService{

    private final MessageRepository messageRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ChatGroupRepository chatGroupRepository;
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private final UserService userService;
    private final DynamicEmailConfig emailConfig;
    private final EmailService emailService;

    public ChatServiceImpl(MessageRepository messageRepository, GroupMemberRepository groupMemberRepository, ChatGroupRepository chatGroupRepository, UserRepository userRepository, ContactRepository contactRepository, UserService userService, DynamicEmailConfig emailConfig, EmailService emailService) {
        this.messageRepository = messageRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.chatGroupRepository = chatGroupRepository;
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.userService = userService;
        this.emailConfig = emailConfig;
        this.emailService = emailService;
    }

    @Override
    public Message saveMessage(UUID groupId, UUID senderId, String content, MessageType type) {
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
                .type(type)
                .build();

        if(type.equals(MessageType.TRIGGER_EVENT)){
            String company = "";
            List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
            List<Contact> contacts = contactRepository.getContactsFromCompany(company);
            for(GroupMember g : members){
                User user = g.getUser();


                String subject = "Request for referral";
                String body = userService.generateEmail(user.getEmail());
                JavaMailSender mailSender = emailConfig.createMailSender(user.getEmail(),user.getAppPassword());
                for(Contact c : contacts){
                    String finalBody = body.replace("<<name>>", c.getName());
                    emailService.SendReferralEmail(c.getEmail(),subject,finalBody,mailSender);
                }
            }
        }

        return messageRepository.save(message);
    }
}
