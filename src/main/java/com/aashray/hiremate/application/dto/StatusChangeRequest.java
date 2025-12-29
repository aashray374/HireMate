package com.aashray.hiremate.application.dto;


import com.aashray.hiremate.application.entity.ApplicationStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class StatusChangeRequest {
    private ApplicationStatus to;
}
