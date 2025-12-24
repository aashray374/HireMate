package com.aashray.hiremate.company.service;

import com.aashray.hiremate.company.entity.Company;
import com.aashray.hiremate.company.repository.CompanyRepository;
import com.aashray.hiremate.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company getCompanyFromName(User user, String name) {
        return null;
    }

    @Override
    public Company getCompanyFromId(User user, Long id) {
        Company company = companyRepository.findById(id).orElse(null);
        if(company != null){
            if(company.getUser().getId().equals(user.getId())){
                return company;
            }
        }
        return null;
    }

    @Override
    public Page<Company> getAllCompanies(User user, Pageable page) {
        return companyRepository.findAllByUser(user,page);
    }

    @Override
    public Company createCompany(Company entity) {
        Company company = companyRepository.findByWebsite(entity.getWebsite()).orElse(null);
        if(company == null){
            return companyRepository.save(entity);
        }
        return null;
    }

    @Override
    public Page<Company> getAllFromLocation(User user, String location, Pageable page) {
        return null;
    }
}
