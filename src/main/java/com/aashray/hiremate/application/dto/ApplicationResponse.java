package com.aashray.hiremate.application.dto;

import com.aashray.hiremate.application.entity.ApplicationPlatform;
import com.aashray.hiremate.application.entity.ApplicationStatus;
import com.aashray.hiremate.company.dto.CompanyResponse;
import com.aashray.hiremate.resume.dto.ResumeMetadata;
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
    private String roleTitle;
    private ApplicationPlatform applicationPlatform;
    private ApplicationStatus status;
    private LocalDate appliedDate;
    private OffsetDateTime createdAt;
}
