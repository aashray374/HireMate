package com.aashray.hiremate.company.controller;


import com.aashray.hiremate.company.dto.CompanyResponse;
import com.aashray.hiremate.company.dto.CreateCompanyRequest;
import com.aashray.hiremate.company.entity.Company;
import com.aashray.hiremate.company.mapper.CompanyMapper;
import com.aashray.hiremate.company.service.CompanyService;
import com.aashray.hiremate.user.entity.User;
import com.aashray.hiremate.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final UserService userService;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    public CompanyController(UserService userService, CompanyService companyService, CompanyMapper companyMapper) {
        this.userService = userService;
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse createCompany(@RequestBody @Valid CreateCompanyRequest companyRequest, Authentication authentication){
        User user = userService.getUserFromEmail(authentication.getName());
        return companyMapper.createResponse(companyService.createCompany(companyMapper.toEntity(companyRequest,user)));
    }

    @GetMapping
    public Page<CompanyResponse> getAllCompanies(Authentication authentication,@PageableDefault(direction = Sort.Direction.ASC, sort = "uploadedAt") Pageable page){
        User user = userService.getUserFromEmail(authentication.getName());
        Page<Company> companies = companyService.getAllCompanies(user,page);

        return companies.map(companyMapper::createResponse);
    }

    @GetMapping("/location/{location}")
    public Page<CompanyResponse> getAllByLocation(@PathVariable String location,Authentication authentication,@PageableDefault(direction = Sort.Direction.ASC, sort = "uploadedAt") Pageable page){
        User user = userService.getUserFromEmail(authentication.getName());
        Page<Company> companies = companyService.getAllFromLocation(user,location,page);

        return companies.map(companyMapper::createResponse);
    }

    @GetMapping("/id/{id}")
    public CompanyResponse getById(@PathVariable Long id,Authentication authentication){
        User user = userService.getUserFromEmail(authentication.getName());
        return companyMapper.createResponse(companyService.getCompanyFromId(user,id));
    }

    @GetMapping("/name/{name}")
    public CompanyResponse getByName(@PathVariable String name,Authentication authentication){
        User user = userService.getUserFromEmail(authentication.getName());
        return companyMapper.createResponse(companyService.getCompanyFromName(user,name));
    }
}
