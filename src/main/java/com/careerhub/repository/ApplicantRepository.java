package com.careerhub.repository;

import com.careerhub.model.Applicants;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends MongoRepository<Applicants, String> {
    Applicants findByEmail(String email);
}

