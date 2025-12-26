package com.aashray.hiremate.application.repository;

import com.aashray.hiremate.application.entity.ApplicationPlatform;
import com.aashray.hiremate.application.entity.ApplicationStatus;
import com.aashray.hiremate.application.entity.Application;
import com.aashray.hiremate.company.entity.Company;
import com.aashray.hiremate.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;


@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {
    Optional<Application> findByCompanyAndUserAndRoleTitleAndStatusIn(Company company, User user, String roleTitle, Collection<ApplicationStatus> status);

    Page<Application> findAllByUser(User user, Pageable pageable);

    Page<Application> findAllByUserAndCompany_Id(User user, Long companyId, Pageable pageable);

    Page<Application> findAllByUserAndPlatform(User user, ApplicationPlatform platform, Pageable pageable);

    Page<Application> findAllByUserAndStatus(User user, ApplicationStatus status, Pageable pageable);

    Page<Application> findAllByUserAndStatusAndPlatform(User user, ApplicationStatus status, ApplicationPlatform platform, Pageable pageable);

    Page<Application> findAllByUserAndStatusAndCompany_Id(User user, ApplicationStatus status, Long company_id, Pageable pageable);

    Page<Application> findAllByUserAndCompany_IdAndPlatform(User user, Long company_id, ApplicationPlatform platform, Pageable pageable);

    Page<Application> findAllByUserAndCompany_IdAndPlatformAndStatus(User user, Long companyId, ApplicationPlatform platform, ApplicationStatus status,Pageable page);
}
