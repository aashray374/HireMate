package com.aashray.hiremate.application.service;

import com.aashray.hiremate.application.entity.Application;
import com.aashray.hiremate.application.entity.ApplicationPlatform;
import com.aashray.hiremate.application.entity.ApplicationStatus;
import com.aashray.hiremate.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationService {
    Application createNewApplication(Application jobApplication);

    Page<Application> getApplications(User user, Pageable pageable, ApplicationStatus status, Long companyId, ApplicationPlatform platform);
}
