package com.aashray.hiremate.application.service;

import com.aashray.hiremate.application.entity.ApplicationStatus;
import com.aashray.hiremate.application.entity.Application;
import com.aashray.hiremate.application.repository.ApplicationRepository;
import com.aashray.hiremate.exception.ApplicationAlreadyExists;
import com.aashray.hiremate.exception.IllegalOwnershipException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository jobRepository;
    private final Collection<ApplicationStatus> status = List.of(
            ApplicationStatus.APPLIED,
            ApplicationStatus.OA,
            ApplicationStatus.INTERVIEW,
            ApplicationStatus.ACCEPTED,
            ApplicationStatus.SELECTED
    );
    public ApplicationServiceImpl(ApplicationRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Application createNewApplication(Application jobApplication) {
        if(jobApplication.getCompany().getUser().getId().equals(jobApplication.getUser().getId()) && jobApplication.getResume().getUser().getId().equals(jobApplication.getResume().getId())){
            Application application = jobRepository.findByCompanyAndUserAndRoleTitleAndStatusIn(
                    jobApplication.getCompany(),
                    jobApplication.getUser(),
                    jobApplication.getRoleTitle(),
                    status
            ).orElse(null);

            if(application == null){
                jobRepository.save(jobApplication);
            }
            throw new ApplicationAlreadyExists();
        }
        throw new IllegalOwnershipException();
    }
}
