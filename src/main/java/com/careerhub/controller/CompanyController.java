package com.careerhub.controller;

import com.careerhub.model.Company;
import com.careerhub.repository.CompanyRepository;
import com.careerhub.request.CompanyLoginRequest;
import com.careerhub.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/careerhub")
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies(){
        List<Company> companies = companyService.findAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/first-n-companies")
    public ResponseEntity<List<Company>> getFirst8Companies(@RequestParam("companyNo") int companyNo) {
        List<Company> companies = companyService.getFirstNCompanies(companyNo);
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

    @GetMapping("/getfield")
    public ResponseEntity<List<Company>> searchFields(@RequestParam("field") String field){
        List<Company> companiesWithSearchedField = companyService.findCompaniesByField(field);

        if (companiesWithSearchedField.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(companiesWithSearchedField);
    }

    @GetMapping("/getCompany/{companyId}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String companyId){
        Optional<Company> companyOpt = companyService.getCompanyById(companyId);

        if (companyOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Company company = companyOpt.get();
        return ResponseEntity.ok(company);
    }

    @PostMapping("/{companyId}/blocklist/{userId}")
    public ResponseEntity<String> addToUserBlocklist(
            @PathVariable String companyId,
            @PathVariable String userId
    ) {
        try {
            companyService.addUserToUserBlocklist(companyId, userId);
            return ResponseEntity.ok("User successfully added to the blocklist for company with id: " + companyId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{companyId}/blocklist")
    public ResponseEntity<List<String>> getBlocklist(@PathVariable String companyId) {
        try {
            List<String> blocklist = companyService.getEmailsOfBlockedApplicants(companyId);
            return ResponseEntity.ok(blocklist);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @PostMapping("/company-login")
    public ResponseEntity<String> login(@RequestBody CompanyLoginRequest loginRequest) {
        Company existingCompany = companyRepository.findCompanyByCompanyLoginEmail(loginRequest.getEmail());

        if (existingCompany != null && loginRequest.getPassword().equals(existingCompany.getCompanyPassword())) {
            return ResponseEntity.ok().body(existingCompany.getCompanyId());
        }

        return ResponseEntity.status(401).body(null);
    }
}
