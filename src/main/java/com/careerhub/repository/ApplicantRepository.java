package com.careerhub.repository;

import com.careerhub.model.Applicants;
import com.careerhub.model.HumanResources;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApplicantRepository extends MongoRepository<Applicants, String> {

}

