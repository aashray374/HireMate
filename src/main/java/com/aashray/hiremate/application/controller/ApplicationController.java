package com.aashray.hiremate.application.controller;


import com.aashray.hiremate.application.dto.ApplicationResponse;
import com.aashray.hiremate.application.dto.CreateApplicationRequest;
import com.aashray.hiremate.application.entity.Application;
import com.aashray.hiremate.application.entity.ApplicationPlatform;
import com.aashray.hiremate.application.entity.ApplicationStatus;
import com.aashray.hiremate.application.mapper.ApplicationMapper;
import com.aashray.hiremate.application.service.ApplicationService;
import com.aashray.hiremate.user.entity.User;
import com.aashray.hiremate.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApplicationController {

    private final UserService userService;
    private final ApplicationMapper applicationMapper;
    private final ApplicationService applicationService;

    public ApplicationController(UserService userService, ApplicationMapper applicationMapper, ApplicationService applicationService) {
        this.userService = userService;
        this.applicationMapper = applicationMapper;
        this.applicationService = applicationService;
    }

    @PostMapping("/applications")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse createNewApplication(@RequestBody @Valid CreateApplicationRequest request, Authentication authentication){
        User user = userService.getUserFromEmail(authentication.getName());
        Application application = applicationMapper.toEntity(request,user, request.getCompanyId(), request.getResumeId());
        return applicationMapper.createResponse(applicationService.createNewApplication(application)) ;
    }

    @GetMapping("/applications")
    public Page<ApplicationResponse> getAllApplications(
            @PageableDefault(size = 20,sort = "appliedDate",direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication,
            @RequestParam(name = "status",required = false)ApplicationStatus status,
            @RequestParam(name = "company",required = false) Long companyId,
            @RequestParam(name = "platform",required = false)ApplicationPlatform platform
    ){
        User user = userService.getUserFromEmail(authentication.getName());
        Page<Application> applications = applicationService.getApplications(user,pageable,status,companyId,platform);
        return applications.map(applicationMapper::createResponse);
    }
}
