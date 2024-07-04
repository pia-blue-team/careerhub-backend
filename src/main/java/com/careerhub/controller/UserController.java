package com.careerhub.controller;

import com.careerhub.model.User;
import com.careerhub.request.UserRegisterRequest;
import com.careerhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;



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
}
