package com.careerhub.repository;

import com.careerhub.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
    List<Job> findByCompanyId(String companyId);

    Optional<Job> findByJobId(String jobId);

    Optional<Job> findByJobTitle(String jobTitle);

}

