package com.squirtle.hiremate.chat.repository;

import com.squirtle.hiremate.chat.entity.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroup, UUID> {}