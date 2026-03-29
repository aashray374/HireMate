package com.squirtle.hiremate.resume.entity;

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
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String url;

    @Column(nullable = false)
    private String publicId;

    @OneToOne(mappedBy = "resume")
    private User user;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}