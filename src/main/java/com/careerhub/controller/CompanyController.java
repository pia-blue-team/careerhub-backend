package com.careerhub.controller;

import com.careerhub.model.Company;
import com.careerhub.repository.CompanyRepository;
import com.careerhub.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/careerhub")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies(){
        List<Company> companies = companyService.findAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/searchCompany")
    public ResponseEntity<List<Company>> searchCompanies(@RequestParam("companyName") String companyName){
        List<Company> companies = companyService.searchCompaniesByName(companyName);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/companies/distinct-fields")
    public ResponseEntity<List<String>> getDistinctFields() {
        List<String> distinctFields = companyService.getDistinctFields();
        return ResponseEntity.ok(distinctFields);
    }

}
