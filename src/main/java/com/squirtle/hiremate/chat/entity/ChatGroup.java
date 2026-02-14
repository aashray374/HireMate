package com.squirtle.hiremate.chat.entity;


import com.squirtle.hiremate.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    private OffsetDateTime createdAt;

    @PrePersist
    public void init() {
        createdAt = OffsetDateTime.now();
    }
}

