package com.aashray.hiremate.application.repository;

import com.aashray.hiremate.application.entity.ApplicationStatus;
import com.aashray.hiremate.application.entity.Application;
import com.aashray.hiremate.company.entity.Company;
import com.aashray.hiremate.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;


@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {
    Optional<Application> findByCompanyAndUserAndRoleTitleAndStatusIn(Company company, User user, String roleTitle, Collection<ApplicationStatus> status);
}
