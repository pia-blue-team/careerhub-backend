package com.careerhub.repository;

import com.careerhub.model.CareerHubUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CareerHubUserRepository extends MongoRepository<CareerHubUser, String> {
    @Query
    public Optional<CareerHubUser> findById(String id);
}
