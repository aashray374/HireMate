package com.aashray.hiremate.application.dto;

import com.aashray.hiremate.application.entity.ApplicationPlatform;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateApplicationRequest {
    @NotNull
    private Long companyId;
    @NotNull
    private Long resumeId;
    @NotNull
    private String roleTitle;
    private ApplicationPlatform platform;
    @NotNull
    private LocalDate appliedAt;
}
