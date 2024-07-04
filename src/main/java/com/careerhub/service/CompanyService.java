package com.careerhub.service;

import com.careerhub.model.Company;
import com.careerhub.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public List<Company> findAllCompanies(){
        return companyRepository.findAll();
    }

    public List<Company> searchCompaniesByName(String companyName){
        return companyRepository.findByCompanyNameContainingIgnoreCase(companyName);
    }
}
