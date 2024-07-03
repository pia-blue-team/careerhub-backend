package com.careerhub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CareerHubUserService {
    private final CareerHubUserRepository userRepository;

    public Optional<CareerHubUser> getUserById(String id) {
        return userRepository.findById(id);
    }
}
