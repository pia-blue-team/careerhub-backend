package com.careerhub.controller;

import com.careerhub.model.Applicants;
import com.careerhub.model.User;
import com.careerhub.request.UserLoginRequest;
import com.careerhub.request.UserRegisterRequest;
import com.careerhub.service.ApplicantService;
import com.careerhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/careerhub")
public class UserController {

    @Autowired
    private UserService userService;

    private ApplicantService applicantService;

    // Might be deleted
    @PostMapping("/register")
    public ResponseEntity<String> RegisterUser(@RequestBody UserRegisterRequest userRegisterRequest){
        boolean isCreated = userService.createNewUser(userRegisterRequest);
        if(isCreated){
            return ResponseEntity.status(HttpStatus.OK).body("User has been created");
        }
        else{
            return ResponseEntity.badRequest().body("There has been an error creating the new User");
        }
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest loginRequest) {
        User existingUser = userService.getUserByEmail(loginRequest.getEmail());

        if (existingUser != null && loginRequest.getPassword().equals(existingUser.getPassword())){
            return ResponseEntity.ok().body(existingUser.getUserId());
        }

        return ResponseEntity.status(401).body(null);
    }


}