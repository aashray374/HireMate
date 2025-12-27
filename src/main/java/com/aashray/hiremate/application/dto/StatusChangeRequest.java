package com.aashray.hiremate.application.dto;


import com.aashray.hiremate.application.entity.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusChangeRequest {
    private ApplicationStatus to;
}
