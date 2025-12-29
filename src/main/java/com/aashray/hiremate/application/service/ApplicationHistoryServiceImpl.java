package com.aashray.hiremate.application.service;


import com.aashray.hiremate.application.entity.Application;
import com.aashray.hiremate.application.entity.ApplicationStatus;
import com.aashray.hiremate.application.entity.ApplicationStatusHistory;
import com.aashray.hiremate.application.repository.ApplicationHistoryRepository;
import com.aashray.hiremate.application.repository.ApplicationRepository;
import com.aashray.hiremate.exception.ApplicationNotFound;
import com.aashray.hiremate.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationHistoryServiceImpl implements ApplicationHistoryService{

    private final ApplicationHistoryRepository applicationHistoryRepository;
    private final ApplicationRepository applicationRepository;

    public ApplicationHistoryServiceImpl(ApplicationHistoryRepository applicationHistoryRepository, ApplicationRepository applicationRepository) {
        this.applicationHistoryRepository = applicationHistoryRepository;
        this.applicationRepository = applicationRepository;
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

    @Override
    public List<ApplicationStatusHistory> findApplicationHistoryById(Long id, User user) {
        Application application = applicationRepository.findById(id).orElse(null);
        if(application == null || !application.getUser().getId().equals(user.getId())){
            throw new ApplicationNotFound();
        }
        return applicationHistoryRepository.findAllByApplication_Id(id);
    }
}
