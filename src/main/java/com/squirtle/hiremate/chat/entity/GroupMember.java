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
@Table(
        name = "group_member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"group_id", "user_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private ChatGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private OffsetDateTime joinedAt;

    @PrePersist
    public void init() {
        joinedAt = OffsetDateTime.now();
    }
}
