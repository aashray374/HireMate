package com.aashray.hiremate.application.dto;

import com.aashray.hiremate.application.entity.ApplicationPlatform;
import com.aashray.hiremate.application.entity.ApplicationStatus;
import com.aashray.hiremate.resume.entity.ResumeLabel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponse {
    private Long id;
    private String company;
    private ResumeLabel resumeLabel;
    private String roleTitle;
    private ApplicationPlatform platform;
    private ApplicationStatus status;
    private LocalDate appliedDate;
    private OffsetDateTime createdAt;
}
