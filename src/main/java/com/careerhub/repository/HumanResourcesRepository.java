package com.careerhub.repository;

import com.careerhub.model.HumanResources;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HumanResourcesRepository extends MongoRepository<HumanResources, String> {

}
