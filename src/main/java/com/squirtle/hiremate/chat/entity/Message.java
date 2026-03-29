package com.squirtle.hiremate.chat.entity;


import com.squirtle.hiremate.chat.dto.MessageType;
import com.squirtle.hiremate.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private ChatGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    private OffsetDateTime sentAt;

    @PrePersist
    public void init() {
        sentAt = OffsetDateTime.now();
    }
}