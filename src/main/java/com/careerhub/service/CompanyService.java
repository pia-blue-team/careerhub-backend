package com.careerhub.service;

import com.careerhub.model.Applicants;
import com.careerhub.model.Company;
import com.careerhub.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public Optional<Company> getCompanyById(String companyId) {
        return companyRepository.findByCompanyId(companyId);
    }

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    public List<Company> getFirstNCompanies(int companyNo) {
        PageRequest pageRequest = PageRequest.of(0, companyNo);
        Page<Company> page = companyRepository.findAll(pageRequest);
        return page.getContent();
    }

    public List<Company> searchCompaniesByName(String companyName) {
        return companyRepository.findByCompanyNameContainingIgnoreCase(companyName);
    }

    public List<Company> findCompaniesByField(String field) {
        return companyRepository.findByField(field);
    }

    public List<String> findDistinctFields() {
        List<Company> companies = companyRepository.findAll();
        List<String> fields = new ArrayList<>();
        for (Company c : companies) {
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

    public boolean isUserBlacklistedByCompany(String companyId, String userId) {
        Optional<Company> companyOptional = companyRepository.findByCompanyId(companyId);
        if (companyOptional.isPresent()) {
            List<String> blacklistedUsers = companyOptional.get().getBlacklistedUsers();
            return blacklistedUsers.contains(userId);
        }

        return false;
    }

    public void addUserToUserBlocklist(String companyId, String userId) throws RuntimeException {
        Company company = companyRepository.findByCompanyId(companyId).orElseThrow();

        List<String> blacklistedUsers = company.getBlacklistedUsers();

        if (blacklistedUsers == null) {
            blacklistedUsers = new ArrayList<>();
        }

        if (!blacklistedUsers.contains(userId)) {
            blacklistedUsers.add(userId);
            company.setBlacklistedUsers(blacklistedUsers);
            companyRepository.save(company);
        } else {
            throw new RuntimeException("The user is already on the blocklist for this company.");
        }
    }
}
