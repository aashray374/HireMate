package com.aashray.hiremate.application.repository;

import com.aashray.hiremate.application.entity.ApplicationStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationHistoryRepository extends JpaRepository<ApplicationStatusHistory,Long> {
}
