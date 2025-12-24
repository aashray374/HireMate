package com.aashray.hiremate.company.service;

import com.aashray.hiremate.company.entity.Company;
import com.aashray.hiremate.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    Company getCompanyFromName(User user, String name);

    Company getCompanyFromId(User user, Long id);

    Page<Company> getAllCompanies(User user, Pageable page);

    Company createCompany(Company entity);

    Page<Company> getAllFromLocation(User user, String location, Pageable page);

}
