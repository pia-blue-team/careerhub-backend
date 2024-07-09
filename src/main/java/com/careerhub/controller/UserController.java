package com.careerhub.controller;

import com.careerhub.model.User;
import com.careerhub.request.UserLoginRequest;
import com.careerhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/careerhub")
public class UserController {

    @Autowired
    private UserService userService;

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

        if (existingUser != null && loginRequest.getPassword().equals(existingUser.getPassword())) {
            return ResponseEntity.ok().body(existingUser.getUserId());
        }

        return ResponseEntity.status(401).body(null);
    }
}