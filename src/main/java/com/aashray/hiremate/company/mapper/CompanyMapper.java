package com.aashray.hiremate.company.mapper;

import com.aashray.hiremate.company.dto.CompanyResponse;
import com.aashray.hiremate.company.dto.CreateCompanyRequest;
import com.aashray.hiremate.company.entity.Company;
import com.aashray.hiremate.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    public Company toEntity(CreateCompanyRequest companyRequest, User user){
        return Company.builder()
                .name(companyRequest.getName())
                .location(companyRequest.getLocation())
                .website(companyRequest.getWebsite())
                .user(user)
                .build();
    }


    public CompanyResponse createResponse(Company company){
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .location(company.getWebsite())
                .website(company.getWebsite())
                .build();
    }

}
