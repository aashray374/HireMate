package com.aashray.hiremate.application.service;

import com.aashray.hiremate.application.entity.Application;
import com.aashray.hiremate.application.entity.ApplicationStatus;
import com.aashray.hiremate.application.entity.ApplicationStatusHistory;

import java.util.List;

public interface ApplicationHistoryService {
    void saveApplicationHistory(Application application, ApplicationStatus from, ApplicationStatus to);

    List<ApplicationStatusHistory> findApplicationHistoryById(Long id);
}
