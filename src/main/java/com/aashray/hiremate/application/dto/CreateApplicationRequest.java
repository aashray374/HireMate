package com.aashray.hiremate.application.dto;

import com.aashray.hiremate.application.entity.ApplicationPlatform;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateApplicationRequest {
    @NotBlank
    private Long companyId;
    @NotBlank
    private Long resumeId;
    @NotBlank
    private String roleTitle;
    private ApplicationPlatform platform;
    @NotBlank
    private LocalDate appliedAt;
}
