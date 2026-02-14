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
@Table(name = "message")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private ChatGroup group;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(columnDefinition = "TEXT")
    private String content;

    private OffsetDateTime sentAt;

    @PrePersist
    public void init() {
        sentAt = OffsetDateTime.now();
    }
}

