package com.careerhub.service;

import com.careerhub.model.Company;
import com.careerhub.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> findDistinctFields(){
        List<Company> companies = companyRepository.findAll();
        List<String> fields = new ArrayList<>();
        for (Company c: companies) {
            fields.add(c.getField());
        }
        return fields;
    }

    public List<String> getDistinctFields() {
        List<Company> companies = companyRepository.findDistinctByField();
        return companies.stream()
                .map(Company::getField)
                .distinct()
                .collect(Collectors.toList());
    }
}
