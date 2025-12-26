package com.aashray.hiremate.application.mapper;

import com.aashray.hiremate.application.dto.ApplicationResponse;
import com.aashray.hiremate.application.dto.CreateApplicationRequest;
import com.aashray.hiremate.application.entity.ApplicationPlatform;
import com.aashray.hiremate.application.entity.ApplicationStatus;
import com.aashray.hiremate.application.entity.Application;
import com.aashray.hiremate.company.entity.Company;
import com.aashray.hiremate.company.service.CompanyService;
import com.aashray.hiremate.resume.entity.Resume;
import com.aashray.hiremate.resume.service.ResumeService;
import com.aashray.hiremate.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    private final ResumeService resumeService;
    private final CompanyService companyService;

    public ApplicationMapper(ResumeService resumeService, CompanyService companyService) {
        this.resumeService = resumeService;
        this.companyService = companyService;
    }

    public Application toEntity(CreateApplicationRequest request, User user,Long companyId,Long resumeId){
        if(request.getPlatform() == null){
            request.setPlatform(ApplicationPlatform.OTHER);
        }
        Resume resume = resumeService.getResumeFromId(user,resumeId);
        Company company = companyService.getCompanyFromId(user,companyId);
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
                .roleTitle(application.getRoleTitle())
                .applicationPlatform(application.getPlatform())
                .status(application.getStatus())
                .appliedDate(application.getAppliedDate())
                .createdAt(application.getCreatedAt())
                .build();
    }
}
