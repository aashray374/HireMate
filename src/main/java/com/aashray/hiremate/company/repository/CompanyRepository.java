package com.aashray.hiremate.company.repository;

import com.aashray.hiremate.company.entity.Company;
import com.aashray.hiremate.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {
    Page<Company> findAllByUserAndLocationIgnoreCase(User user, String location, Pageable pageable);

    Optional<Company> findByUserAndNameIgnoreCase(User user, String name);

    Page<Company> findAllByUser(User user, Pageable pageable);
}
