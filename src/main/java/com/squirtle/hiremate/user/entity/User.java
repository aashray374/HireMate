package com.squirtle.hiremate.user.entity;

import com.squirtle.hiremate.common.config.CryptoConverter;
import com.squirtle.hiremate.resume.entity.Resume;
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
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Column(updatable = false)
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private String template;

    @Convert(converter = CryptoConverter.class)
    private String appPassword;

    @PrePersist
    public void init() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void update() {
        this.updatedAt = OffsetDateTime.now();
    }
}