package com.aashray.hiremate.application.mapper;

import com.aashray.hiremate.application.dto.ApplicationResponse;
import com.aashray.hiremate.application.dto.CreateApplicationRequest;
import com.aashray.hiremate.application.entity.ApplicationPlatform;
import com.aashray.hiremate.application.entity.ApplicationStatus;
import com.aashray.hiremate.application.entity.Application;
import com.aashray.hiremate.company.entity.Company;
import com.aashray.hiremate.company.mapper.CompanyMapper;
import com.aashray.hiremate.resume.entity.Resume;
import com.aashray.hiremate.resume.mapper.ResumeMapper;
import com.aashray.hiremate.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    private final CompanyMapper companyMapper;
    private final ResumeMapper resumeMapper;

    public ApplicationMapper(CompanyMapper companyMapper, ResumeMapper resumeMapper) {
        this.companyMapper = companyMapper;
        this.resumeMapper = resumeMapper;
    }

    public Application toEntity(CreateApplicationRequest request, Company company, Resume resume, User user){
        if(request.getPlatform() == null){
            request.setPlatform(ApplicationPlatform.OTHER);
        }
        return Application.builder()
                .user(user)
                .company(company)
                .resume(resume)
                .roleTitle(request.getRoleTitle())
                .platform(request.getPlatform())
                .status(ApplicationStatus.APPLIED)
                .appliedDate(request.getAppliedAt())
                .build();
    }


    public ApplicationResponse createResponse(Application application){
        return ApplicationResponse.builder()
                .id(application.getId())
                .company(companyMapper.createResponse(application.getCompany()))
                .resume(resumeMapper.createResponse(application.getResume()))
                .roleTitle(application.getRoleTitle())
                .applicationPlatform(application.getPlatform())
                .status(application.getStatus())
                .appliedDate(application.getAppliedDate())
                .createdAt(application.getCreatedAt())
                .build();
    }
}
