package com.careerhub.controller;

import com.careerhub.model.Applicants;
import com.careerhub.model.User;
import com.careerhub.service.ApplicantService;
import com.careerhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/download-cv/{id}")
    public ResponseEntity<Resource> downloadCv(@PathVariable String id) {
        try {
            // Fetch the user's CV path from the database
            Optional<Applicants> optionalApplicants = applicantService.getApplicantByUserId(id);

            if (optionalApplicants.isEmpty()) return ResponseEntity.notFound().build();

            Applicants applicant = optionalApplicants.get();

            if (applicant.getCvPath() == null) return ResponseEntity.notFound().build();

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
            Applicants existingApplicant = applicantService.getApplicantByEmail(email);
            if (existingApplicant != null) return ResponseEntity.status(409).body(null);

            // Otherwise, just create a new user.
            String aboutUser = "Lorem ipsum dolor sit amet.";
            String currentRole = "Frontend Developer";
            User user = applicantService.saveApplicantWithCv(name, surname, password, email, aboutUser, currentRole, file);
            return ResponseEntity.ok(user.getUserId());
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
