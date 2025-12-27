package com.aashray.hiremate.application.service;

import com.aashray.hiremate.application.entity.Application;
import com.aashray.hiremate.application.entity.ApplicationStatus;

public interface ApplicationHistoryService {
    void saveApplicationHistory(Application application, ApplicationStatus from, ApplicationStatus to);
}
