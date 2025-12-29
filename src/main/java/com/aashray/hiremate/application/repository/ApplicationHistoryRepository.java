package com.aashray.hiremate.application.repository;

import com.aashray.hiremate.application.entity.ApplicationStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationHistoryRepository extends JpaRepository<ApplicationStatusHistory,Long> {

    List<ApplicationStatusHistory> findAllByApplication_Id(Long applicationId);
}
