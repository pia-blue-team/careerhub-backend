package com.careerhub.controller;

import com.careerhub.model.User;
import com.careerhub.request.UserLoginRequest;
import com.careerhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

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

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> register(
            @RequestPart String name,
            @RequestPart String surname,
            @RequestPart String email,
            @RequestPart String password,
            @RequestPart MultipartFile file
    ) {
        try {
            // First, check if there is an existing user sharing the same email.
            User existingUser = userService.getUserByEmail(email);
            if (existingUser != null) return ResponseEntity.status(409).body(null);

            // Otherwise, just create a new user.
            String aboutUser = "Lorem ipsum dolor sit amet.";
            String currentRole = "Frontend Developer";
            User user = userService.saveUserWithCv(name, surname, password, email, aboutUser, currentRole, file);
            return ResponseEntity.ok(user.getUserId());
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/download-cv/{userId}")
    public ResponseEntity<Resource> downloadCv(@PathVariable String userId) {
        try {
            // Fetch the user's CV path from the database
            User user = userService.getUserByUserId(userId).get();
            if (user == null || user.getCvPath() == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get(user.getCvPath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName().toString() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(404).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/userProfile/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable String userId) {
        Optional<User> userOpt = userService.getUserByUserId(userId);

        if (!userOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        return ResponseEntity.ok(user);

    }

    @PostMapping("/apply/{userId}/{jobId}")
    public ResponseEntity<User> applyForJob(@PathVariable String userId, @PathVariable String jobId) {
        User user = userService.applyForJob(userId, jobId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}