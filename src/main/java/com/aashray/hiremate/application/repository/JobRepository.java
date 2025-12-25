package com.aashray.hiremate.application.repository;

import com.aashray.hiremate.application.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<JobApplication,Long> {
}
