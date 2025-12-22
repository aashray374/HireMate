package com.aashray.hiremate.resume.dto;

import com.aashray.hiremate.resume.entity.ResumeLabel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadRequest {
    private ResumeLabel label;

    public UploadRequest(ResumeLabel label) {
        this.label = label;
    }

    public UploadRequest() {
    }
}
