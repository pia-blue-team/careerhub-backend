package com.careerhub.controller;

import com.careerhub.model.Applicants;
import com.careerhub.model.User;
import com.careerhub.service.ApplicantService;
import com.careerhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
@RequestMapping("/careerhub")
public class ApplicantController {

    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private UserService userService;

    @PostMapping("/apply/{userId}/{jobId}")
    public ResponseEntity<Applicants> applyForJob(@PathVariable String userId, @PathVariable String jobId) {
        Applicants applicant = applicantService.applyForJob(userId, jobId);
        if (applicant != null) {
            return ResponseEntity.ok(applicant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/download-cv/{userId}")
    public ResponseEntity<Resource> downloadCv(@PathVariable String userId) {
        try {
            // Fetch the user's CV path from the database
            Applicants applicant = (Applicants) userService.getUserByUserId(userId).get();


            if (applicant == null || applicant.getCvPath() == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get(applicant.getCvPath());
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
            Applicants applicant = applicantService.saveUserWithCv(firstName, lastName, email, password, aboutUser, currentRole, cvFile);
            return ResponseEntity.ok(applicant);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/userProfile/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable String userId){
        Optional<User> userOpt = userService.getUserByUserId(userId);

        if (!userOpt.isPresent()){
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        return ResponseEntity.ok(user);

    }

}
