package com.aashray.hiremate.application.service;


import com.aashray.hiremate.application.entity.Application;
import com.aashray.hiremate.application.entity.ApplicationStatus;
import com.aashray.hiremate.application.entity.ApplicationStatusHistory;
import com.aashray.hiremate.application.repository.ApplicationHistoryRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplicationHistoryServiceImpl implements ApplicationHistoryService{

    private final ApplicationHistoryRepository applicationHistoryRepository;

    public ApplicationHistoryServiceImpl(ApplicationHistoryRepository applicationHistoryRepository) {
        this.applicationHistoryRepository = applicationHistoryRepository;
    }

    @Override
    public void saveApplicationHistory(Application application, ApplicationStatus from, ApplicationStatus to){
        ApplicationStatusHistory applicationStatusHistory = ApplicationStatusHistory.builder()
                .application(application)
                .fromStatus(from)
                .toStatus(to)
                .build();

        applicationHistoryRepository.save(applicationStatusHistory);
    }
}
