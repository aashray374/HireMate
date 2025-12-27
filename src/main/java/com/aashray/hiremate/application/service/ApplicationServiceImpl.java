package com.aashray.hiremate.application.service;

import com.aashray.hiremate.application.entity.ApplicationPlatform;
import com.aashray.hiremate.application.entity.ApplicationStatus;
import com.aashray.hiremate.application.entity.Application;
import com.aashray.hiremate.application.repository.ApplicationRepository;
import com.aashray.hiremate.exception.ApplicationAlreadyExists;
import com.aashray.hiremate.exception.ApplicationNotFound;
import com.aashray.hiremate.exception.IllegalOwnershipException;
import com.aashray.hiremate.exception.IllegalStateTransition;
import com.aashray.hiremate.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository jobRepository;
    private final ApplicationHistoryService applicationHistoryService;
    private final Collection<ApplicationStatus> status = List.of(
            ApplicationStatus.APPLIED,
            ApplicationStatus.OA,
            ApplicationStatus.INTERVIEW,
            ApplicationStatus.ACCEPTED,
            ApplicationStatus.OFFER
    );
    public ApplicationServiceImpl(ApplicationRepository jobRepository, ApplicationHistoryService applicationHistoryService) {
        this.jobRepository = jobRepository;
        this.applicationHistoryService = applicationHistoryService;
    }

    @Override
    public Application createNewApplication(Application jobApplication) {
        if(jobApplication.getCompany().getUser().getId().equals(jobApplication.getUser().getId()) && jobApplication.getResume().getUser().getId().equals(jobApplication.getUser().getId())){
            Application application = jobRepository.findByCompanyAndUserAndRoleTitleAndStatusIn(
                    jobApplication.getCompany(),
                    jobApplication.getUser(),
                    jobApplication.getRoleTitle(),
                    status
            ).orElse(null);

            if(application == null){
                return jobRepository.save(jobApplication);
            }
            throw new ApplicationAlreadyExists();
        }
        throw new IllegalOwnershipException();
    }

    @Override
    public Page<Application> getApplications(User user, Pageable pageable, ApplicationStatus status, Long companyId, ApplicationPlatform platform) {
        if(status != null && platform != null && companyId != null){
            return jobRepository.findAllByUserAndCompany_IdAndPlatformAndStatus(user,companyId,platform,status,pageable);
        }
        if(status == null && platform != null && companyId != null){
            return jobRepository.findAllByUserAndCompany_IdAndPlatform(user,companyId,platform,pageable);
        }
        if(status != null && platform == null && companyId != null){
            return jobRepository.findAllByUserAndStatusAndCompany_Id(user,status,companyId,pageable);
        }
        if(status != null && platform != null){
            return jobRepository.findAllByUserAndStatusAndPlatform(user, status, platform, pageable);
        }
        if(status != null){
            return jobRepository.findAllByUserAndStatus(user, status, pageable);
        }
        if(platform != null){
            return jobRepository.findAllByUserAndPlatform(user, platform, pageable);
        }
        if(companyId != null){
            return jobRepository.findAllByUserAndCompany_Id(user, companyId, pageable);
        }
        return jobRepository.findAllByUser(user,pageable);
    }

    @Override
    public Application stateTransition(User user, Long applicationId, ApplicationStatus newStatus) {
        Application application = jobRepository.findById(applicationId).orElse(null);

        if(application == null || !application.getUser().getId().equals(user.getId())){
            throw new ApplicationNotFound();
        }

        if(!application.getStatus().canTransitionTo(newStatus)){
            throw new IllegalStateTransition(application.getStatus(),newStatus);
        }
        ApplicationStatus from = application.getStatus();
        application.setStatus(newStatus);
        applicationHistoryService.saveApplicationHistory(application,from,newStatus);
        return jobRepository.save(application);
    }
}
