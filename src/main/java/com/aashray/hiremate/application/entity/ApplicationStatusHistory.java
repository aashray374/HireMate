package com.aashray.hiremate.application.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Application application;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus fromStatus;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus toStatus;

    private OffsetDateTime changedAt;

    private String note;

    @PrePersist
    private void init(){
        this.changedAt = OffsetDateTime.now();
    }
}

