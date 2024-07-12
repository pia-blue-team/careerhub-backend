package com.careerhub.controller;

import com.careerhub.model.Applicants;
import com.careerhub.model.User;
import com.careerhub.request.UserLoginRequest;
import com.careerhub.service.ApplicantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
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

@Tag(name = "Applicant Management", description = "APIs for managing applicant operations")
@Controller
@RequestMapping("/careerhub")
public class ApplicantController {

    @Autowired
    private ApplicantService applicantService;

    @Operation(summary = "Download CV", description = "Downloads the CV of an applicant by their user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CV downloaded successfully"),
            @ApiResponse(responseCode = "404", description = "Applicant or CV not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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

    @Operation(summary = "Register applicant", description = "Registers a new applicant with their details and CV")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applicant registered successfully"),
            @ApiResponse(responseCode = "409", description = "Applicant already registered"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> register(
            @Valid @RequestPart(name = "name") String name,
            @Valid @RequestPart(name = "surname") String surname,
            @Valid @RequestPart(name = "email") @Email(message = "Invalid email format") String email,
            @Valid @RequestPart(name = "password") @Size(min = 8, max = 12, message = "Password must be between 8 and 12 characters") String password,
            @RequestPart MultipartFile file
    ) {
        try {
            // First, check if there is an existing user sharing the same email.
            Applicants existingApplicant = applicantService.getApplicantByEmail(email);
            if (existingApplicant != null) return ResponseEntity.status(409).body("This user is already registered!");

            // Otherwise, just create a new user.
            String aboutUser = "Lorem ipsum dolor sit amet.";
            String currentRole = "Frontend Developer";
            User user = applicantService.saveApplicantWithCv(name, surname, password, email, aboutUser, currentRole, file);
            return ResponseEntity.ok(user.getUserId());
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Get user profile by user ID", description = "Retrieves the profile of a user by their user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/userProfile/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable String userId){
        Optional<Applicants> applicantOptional = applicantService.getApplicantByUserId(userId);

        if (applicantOptional.isEmpty()) return ResponseEntity.notFound().build();

        Applicants applicant = applicantOptional.get();
        return ResponseEntity.ok(applicant);
    }

    @Operation(summary = "Applicant login", description = "Authenticates an applicant using email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest loginRequest) {
        Applicants existingApplicant = applicantService.getApplicantByEmail(loginRequest.getEmail());

        if (existingApplicant != null && loginRequest.getPassword().equals(existingApplicant.getPassword())) {
            return ResponseEntity.ok().body(existingApplicant.getUserId());
        }

        return ResponseEntity.status(401).body(null);
    }
}
