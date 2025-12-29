package com.aashray.hiremate.application.controller;


import com.aashray.hiremate.application.dto.ApplicationHistoryResponse;
import com.aashray.hiremate.application.dto.ApplicationResponse;
import com.aashray.hiremate.application.dto.CreateApplicationRequest;
import com.aashray.hiremate.application.dto.StatusChangeRequest;
import com.aashray.hiremate.application.entity.Application;
import com.aashray.hiremate.application.entity.ApplicationPlatform;
import com.aashray.hiremate.application.entity.ApplicationStatus;
import com.aashray.hiremate.application.entity.ApplicationStatusHistory;
import com.aashray.hiremate.application.mapper.ApplicationHistoryMapper;
import com.aashray.hiremate.application.mapper.ApplicationMapper;
import com.aashray.hiremate.application.service.ApplicationHistoryService;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("applications")
public class ApplicationController {

    private final UserService userService;
    private final ApplicationMapper applicationMapper;
    private final ApplicationService applicationService;
    private final ApplicationHistoryService applicationHistoryService;
    private final ApplicationHistoryMapper applicationHistoryMapper;

    public ApplicationController(UserService userService, ApplicationMapper applicationMapper, ApplicationService applicationService, ApplicationHistoryService applicationHistoryService, ApplicationHistoryMapper applicationHistoryMapper) {
        this.userService = userService;
        this.applicationMapper = applicationMapper;
        this.applicationService = applicationService;
        this.applicationHistoryService = applicationHistoryService;
        this.applicationHistoryMapper = applicationHistoryMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse createNewApplication(@RequestBody @Valid CreateApplicationRequest request, Authentication authentication){
        User user = userService.getUserFromEmail(authentication.getName());
        Application application = applicationMapper.toEntity(request,user, request.getCompanyId(), request.getResumeId());
        return applicationMapper.createResponse(applicationService.createNewApplication(application)) ;
    }

    @GetMapping
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

    @PatchMapping("/{id}/status")
    public ApplicationResponse changeApplicationStatus(@PathVariable Long id, @RequestBody StatusChangeRequest request,Authentication authentication){
        User user = userService.getUserFromEmail(authentication.getName());
        return  applicationMapper.createResponse(applicationService.stateTransition(user,id,request.getTo()));
    }

    @GetMapping("/{id}/history")
    public List<ApplicationHistoryResponse> getApplicationHistory(@PathVariable Long id,Authentication authentication){
        User user = userService.getUserFromEmail(authentication.getName());
        List<ApplicationStatusHistory>  history = applicationHistoryService.findApplicationHistoryById(id,user);
        List<ApplicationHistoryResponse> responseList = new ArrayList<>();

        for(ApplicationStatusHistory h : history){
            responseList.add(applicationHistoryMapper.createResponse(h));
        }
        return responseList;
    }
}
