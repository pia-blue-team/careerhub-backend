package com.careerhub.controller;

import com.careerhub.model.User;
import com.careerhub.request.UserLoginRequest;
import com.careerhub.service.UserService;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/careerhub")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable @Email String email) {
        User user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}