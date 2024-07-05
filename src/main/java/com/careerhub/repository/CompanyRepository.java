package com.careerhub.repository;

import com.careerhub.model.Company;
import com.careerhub.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {
    List<Company> findByCompanyNameContainingIgnoreCase(String companyName);
    Optional<Company> findByCompanyId(String companyId);

    @Query(value = "{}", fields = "{ 'field': 1 }")
    List<Company> findDistinctByField();
}
