package com.careerhub.repository;

import com.careerhub.model.Company;
import com.careerhub.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {
    List<Company> findByCompanyNameContainingIgnoreCase(String companyName);
    Optional<Company> findByCompanyId(String companyId);
}
