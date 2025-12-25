package com.aashray.hiremate.company.service;

import com.aashray.hiremate.company.entity.Company;
import com.aashray.hiremate.company.repository.CompanyRepository;
import com.aashray.hiremate.exception.CompanyAlreadyExists;
import com.aashray.hiremate.exception.CompanyNotFound;
import com.aashray.hiremate.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company getCompanyFromName(User user, String name) {
        return companyRepository
                .findByUserAndNameIgnoreCase(user, name)
                .orElseThrow(() -> new CompanyNotFound("The company with name: "+name+" does not exists"));

    }

    @Override
    public Company getCompanyFromId(User user, Long id) {
        Company company = companyRepository.findById(id).orElse(null);
        if(company != null){
            if(company.getUser().getId().equals(user.getId())){
                return company;
            }
        }
        throw new CompanyNotFound("The Company With id: "+id+" does not exists");
    }

    @Override
    public Page<Company> getAllCompanies(User user, Pageable page) {
        return companyRepository.findAllByUser(user,page);
    }

    @Override
    public Company createCompany(User user,Company entity) {
        Optional<Company> company = companyRepository.findByUserAndNameIgnoreCase(user, entity.getName());
        if(company.isEmpty()){
            return companyRepository.save(entity);
        }
        throw new CompanyAlreadyExists("The Company Already Exists");
    }

    @Override
    public Page<Company> getAllFromLocation(User user, String location, Pageable page) {
        return companyRepository.findAllByUserAndLocationIgnoreCase(user,location,page);
    }
}
