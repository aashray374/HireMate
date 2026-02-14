package com.squirtle.hiremate.chat.repository;

import com.squirtle.hiremate.chat.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, UUID> {

    boolean existsByGroupIdAndUserId(UUID groupId, UUID userId);

    List<GroupMember> findByGroupId(UUID groupId);
}