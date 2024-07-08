package com.careerhub.controller;

import com.careerhub.model.User;
import com.careerhub.request.UserLoginRequest;
import com.careerhub.request.UserRegisterRequest;
import com.careerhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/careerhub")
public class UserController {

    @Autowired
    private UserService userService;

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
    public ResponseEntity<Integer> login(@RequestBody UserLoginRequest loginRequest) {
        User existingUser = userService.getUserByEmail(loginRequest.getEmail());

        if (existingUser != null && loginRequest.getPassword().equals(existingUser.getPassword())){
            return ResponseEntity.ok().body(existingUser.getUserId());
        }

        return ResponseEntity.status(401).body(null);
    }

    @PostMapping("/upload-cv")
    public ResponseEntity<User> register(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("aboutUser") String aboutUser,
            @RequestParam("currentRole") String currentRole,
            @RequestParam("cvFile") MultipartFile cvFile) {

        try {
            User user = userService.saveUserWithCv(firstName, lastName, email, password, aboutUser, currentRole, cvFile);
            return ResponseEntity.ok(user);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/userProfile/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable int userId){
        Optional<User> userOpt = userService.getUserByUserId(userId);

        if (!userOpt.isPresent()){
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        return ResponseEntity.ok(user);

    }
}