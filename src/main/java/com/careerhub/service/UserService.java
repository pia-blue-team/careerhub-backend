package com.careerhub.service;

import com.careerhub.model.User;
import com.careerhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByUserId(String id) {
        return userRepository.findUserByUserId(id);
    }

    public User findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}