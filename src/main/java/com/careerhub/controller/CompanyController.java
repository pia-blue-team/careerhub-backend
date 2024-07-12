package com.careerhub.controller;

import com.careerhub.model.Company;
import com.careerhub.repository.CompanyRepository;
import com.careerhub.request.CompanyLoginRequest;
import com.careerhub.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "Company Management", description = "APIs for managing company operations")
@Controller
@RequestMapping("/careerhub")
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;

    @Operation(summary = "Get all companies", description = "Retrieves a list of all companies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of companies retrieved successfully")
    })
    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies(){
        List<Company> companies = companyService.findAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @Operation(summary = "Get the first N companies", description = "Retrieves the first N companies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of companies retrieved successfully")
    })
    @GetMapping("/first-n-companies")
    public ResponseEntity<List<Company>> getFirst8Companies(@RequestParam("companyNo") int companyNo) {
        List<Company> companies = companyService.getFirstNCompanies(companyNo);
        return ResponseEntity.ok(companies);
    }

    @Operation(summary = "Search companies by name", description = "Searches for companies by their name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of companies retrieved successfully")
    })
    @GetMapping("/searchCompany")
    public ResponseEntity<List<Company>> searchCompanies(@RequestParam("companyName") String companyName){
        List<Company> companies = companyService.searchCompaniesByName(companyName);
        return ResponseEntity.ok(companies);
    }

    @Operation(summary = "Get distinct fields", description = "Retrieves a list of distinct fields of companies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of distinct fields retrieved successfully")
    })
    @GetMapping("/companies/distinct-fields")
    public ResponseEntity<List<String>> getDistinctFields() {
        List<String> distinctFields = companyService.getDistinctFields();
        return ResponseEntity.ok(distinctFields);
    }

    @Operation(summary = "Search companies by field", description = "Searches for companies by their field")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of companies retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Field not found")
    })
    @GetMapping("/getfield")
    public ResponseEntity<List<Company>> searchFields(@RequestParam("field") String field){
        List<Company> companiesWithSearchedField = companyService.findCompaniesByField(field);

        if (companiesWithSearchedField.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(companiesWithSearchedField);
    }

    @Operation(summary = "Get company by ID", description = "Retrieves a specific company by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    @GetMapping("/getCompany/{companyId}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String companyId){
        Optional<Company> companyOpt = companyService.getCompanyById(companyId);

        if (companyOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Company company = companyOpt.get();
        return ResponseEntity.ok(company);
    }

    @Operation(summary = "Add user to blocklist", description = "Adds a user to the company's blocklist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User added to blocklist successfully"),
            @ApiResponse(responseCode = "400", description = "Failed to add user to blocklist")
    })
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

    @Operation(summary = "Get blocklist by company ID", description = "Retrieves the blocklist for a specific company ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Blocklist retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/{companyId}/blocklist")
    public ResponseEntity<List<String>> getBlocklist(@PathVariable String companyId) {
        try {
            List<String> blocklist = companyService.getEmailsOfBlockedApplicants(companyId);
            return ResponseEntity.ok(blocklist);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @Operation(summary = "Company login", description = "Authenticates a company using email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company login successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping("/company-login")
    public ResponseEntity<String> login(@RequestBody CompanyLoginRequest loginRequest) {
        Company existingCompany = companyRepository.findCompanyByCompanyLoginEmail(loginRequest.getEmail());

        if (existingCompany != null && loginRequest.getPassword().equals(existingCompany.getCompanyPassword())) {
            return ResponseEntity.ok().body(existingCompany.getCompanyId());
        }

        return ResponseEntity.status(401).body(null);
    }
}
