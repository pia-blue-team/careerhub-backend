package com.careerhub.service;

import com.careerhub.model.User;
import com.careerhub.repository.UserRepository;
import com.careerhub.request.UserRegisterRequest;
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


    // Might be deleted
    public boolean createNewUser(UserRegisterRequest userRegisterRequest){
        User newUser = new User();

        User emailExists = userRepository.findByEmail(userRegisterRequest.getEmail());
        if (emailExists != null) {
            return false;
        }
        else {
            newUser.setName(userRegisterRequest.getName());
            newUser.setEmail(userRegisterRequest.getEmail());
            newUser.setSurname(userRegisterRequest.getSurname());
            newUser.setPassword(userRegisterRequest.getPassword());
            userRepository.save(newUser);
            return true;
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByUserId(String id){
        return userRepository.findUserByUserId(id);
    }


    public User findByEmailAndPassword(String email,String password) {
        return userRepository.findByEmailAndPassword(email,password);
    }


}