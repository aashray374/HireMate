package com.squirtle.hiremate.chat.service;

import com.squirtle.hiremate.chat.entity.ChatGroup;
import com.squirtle.hiremate.chat.entity.GroupMember;
import com.squirtle.hiremate.chat.repository.ChatGroupRepository;
import com.squirtle.hiremate.chat.repository.GroupMemberRepository;
import com.squirtle.hiremate.user.entity.User;
import com.squirtle.hiremate.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChatGroupServiceImpl implements ChatGroupService {

    private final ChatGroupRepository chatGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    public ChatGroupServiceImpl(ChatGroupRepository chatGroupRepository, GroupMemberRepository groupMemberRepository, UserRepository userRepository) {
        this.chatGroupRepository = chatGroupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public ChatGroup createGroup(String groupName, String creatorEmail) {
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatGroup group = ChatGroup.builder()
                .name(groupName)
                .createdBy(creator)
                .build();

        ChatGroup savedGroup = chatGroupRepository.save(group);

        GroupMember member = GroupMember.builder()
                .group(savedGroup)
                .user(creator)
                .build();

        groupMemberRepository.save(member);

        return savedGroup;
    }

    @Override
    @Transactional
    public void addMember(UUID groupId, String requestingUserEmail, String newMemberEmail) {
        User requester = userRepository.findByEmail(requestingUserEmail)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        User newMember = userRepository.findByEmail(newMemberEmail)
                .orElseThrow(() -> new RuntimeException("User to add not found"));

        if (!groupMemberRepository
                .existsByGroupIdAndUserId(groupId, requester.getId())) {
            throw new RuntimeException("You are not a member of this group");
        }

        if (groupMemberRepository
                .existsByGroupIdAndUserId(groupId, newMember.getId())) {
            throw new RuntimeException("User already in group");
        }

        ChatGroup group = chatGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        GroupMember member = GroupMember.builder()
                .group(group)
                .user(newMember)
                .build();

        groupMemberRepository.save(member);
    }
}
