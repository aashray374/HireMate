package com.aashray.hiremate.application.controller;


import com.aashray.hiremate.application.dto.ApplicationResponse;
import com.aashray.hiremate.application.dto.CreateApplicationRequest;
import com.aashray.hiremate.application.entity.Application;
import com.aashray.hiremate.application.mapper.ApplicationMapper;
import com.aashray.hiremate.application.service.ApplicationService;
import com.aashray.hiremate.company.entity.Company;
import com.aashray.hiremate.company.service.CompanyService;
import com.aashray.hiremate.resume.entity.Resume;
import com.aashray.hiremate.resume.service.ResumeService;
import com.aashray.hiremate.user.entity.User;
import com.aashray.hiremate.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    private final UserService userService;
    private final CompanyService companyService;
    private final ResumeService resumeService;
    private final ApplicationMapper applicationMapper;
    private final ApplicationService applicationService;

    public ApplicationController(UserService userService, CompanyService companyService, ResumeService resumeService, ApplicationMapper applicationMapper, ApplicationService applicationService) {
        this.userService = userService;
        this.companyService = companyService;
        this.resumeService = resumeService;
        this.applicationMapper = applicationMapper;
        this.applicationService = applicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse createNewApplication(@RequestBody @Valid CreateApplicationRequest request, Authentication authentication){
        User user = userService.getUserFromEmail(authentication.getName());
        Company company = companyService.getCompanyFromId(user, request.getCompanyId());
        Resume resume = resumeService.getResumeFromId(user, request.getResumeId());
        Application application = applicationMapper.toEntity(request,company,resume,user);
        return applicationMapper.createResponse(applicationService.createNewApplication(application)) ;
    }
}
