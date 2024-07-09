package com.careerhub.repository;

import com.careerhub.model.Counter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CounterRepository extends MongoRepository<Counter, String> {

    Optional<Counter> findById(String id);
}
