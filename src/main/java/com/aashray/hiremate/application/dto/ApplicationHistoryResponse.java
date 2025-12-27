package com.aashray.hiremate.application.dto;

import com.aashray.hiremate.application.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationHistoryResponse {
    private ApplicationStatus from;
    private ApplicationStatus to;
    private OffsetDateTime changedAt;
}
