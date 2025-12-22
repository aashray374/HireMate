package com.aashray.hiremate.resume.mapper;

import com.aashray.hiremate.resume.dto.ResumeMetadata;
import com.aashray.hiremate.resume.entity.Resume;
import org.springframework.stereotype.Component;

@Component
public class ResumeMapper {

    public ResumeMetadata createResponse(Resume resume){
        return ResumeMetadata.builder()
                .id(resume.getId())
                .originalFileName(resume.getOriginalFileName())
                .storedFileName(resume.getStoredFileName())
                .uploadedAt(resume.getUploadedAt())
                .label(resume.getLabel())
                .build();
    }

}
