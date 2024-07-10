package com.careerhub.repository;

import com.careerhub.model.Applicants;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicantRepository extends MongoRepository<Applicants, String> {
    Optional<Applicants> findByUserId(String userId);
    Applicants findByEmail(String email);
}

