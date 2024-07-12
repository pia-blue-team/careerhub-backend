package com.careerhub.controller;

import com.careerhub.model.Company;
import com.careerhub.model.CustomCompanyJobsResponse;
import com.careerhub.model.Job;
import com.careerhub.request.CreateJobRequest;
import com.careerhub.service.CompanyService;
import com.careerhub.request.JobRequest;
import com.careerhub.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@Tag(name = "Job Management", description = "APIs for managing job operations")
@Controller
@RequestMapping("/careerhub")
public class JobController {
    @Autowired
    private JobService jobService;
    @Autowired
    private CompanyService companyService;

    @Operation(summary = "Create a new job", description = "Creates a new job and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/createJob")
    public ResponseEntity<Job> createJob(@Valid @RequestBody CreateJobRequest request) {
        Job createdJob = jobService.createJob(request);
        return ResponseEntity.ok(createdJob);
    }

    @Operation(summary = "Delete a job", description = "Deletes a job by its ID and associated company ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Job not found")
    })
    @DeleteMapping("/deleteJob/{companyId}/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable String companyId, @PathVariable String jobId) {
        try {
            jobService.deleteJob(companyId, jobId);
            return ResponseEntity.ok("Job deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Get applicants by job ID", description = "Gets a list of applicants for a specific job ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of applicants retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Job not found")
    })
    @GetMapping("/jobs/{jobId}/applicants")
    public ResponseEntity<List<String>> getApplicantsByJobId(@PathVariable String jobId) {
        List<String> applicants = jobService.getApplicantsByJobId(jobId);
        if (applicants != null) {
            return ResponseEntity.ok(applicants);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Get jobs by company ID", description = "Gets a list of jobs for a specific company ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of jobs retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    @GetMapping("/jobs/{companyId}")
    public ResponseEntity<CustomCompanyJobsResponse> getJobsByCompanyId(@PathVariable String companyId){
        List<Job> jobsOfCompany = jobService.getJobsByCompanyId(companyId);
        Optional<Company> companyOpt = companyService.getCompanyById(companyId);
        //System.out.println(companyOpt);

        if (companyOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Company company = companyOpt.get();

        CustomCompanyJobsResponse response = new CustomCompanyJobsResponse(company, jobsOfCompany);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get job details by job ID", description = "Gets detailed information about a specific job")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Job not found")
    })
    @GetMapping("/jobdetails/{jobId}")
    public ResponseEntity<Job> getJobByJobId(@PathVariable String jobId){
        Optional<Job> jobOpt = jobService.getJobById(jobId);
        if (jobOpt.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Job job = jobOpt.get();
        return ResponseEntity.ok(job);
    }

    @Operation(summary = "Get applied jobs for a user", description = "Gets a list of jobs applied by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of applied jobs retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}/getAppliedJobs")
    public ResponseEntity<List<Job>> getAppliedJobs(@PathVariable String userId) {
        try {
            List<Job> appliedJobs = jobService.getAppliedJobs(userId);
            return ResponseEntity.ok(appliedJobs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Apply for a job and check blocked status", description = "Applies for a job and checks if the user is blocked by the company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job application successful"),
            @ApiResponse(responseCode = "400", description = "Job application failed")
    })
    @PostMapping("/apply-check")
    public Job applyAndCheckBlocked(@RequestBody JobRequest jobRequest){
        return jobService.apply(jobRequest.getJobId(), jobRequest.getUserId()).getBody();
    }

    @Operation(summary = "Apply for a job", description = "Applies for a specific job by the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job application successful"),
            @ApiResponse(responseCode = "400", description = "Job application failed")
    })
    @PostMapping("/apply/{applicantId}/{jobId}")
    public ResponseEntity<String> applyForJob(@PathVariable String applicantId, @PathVariable String jobId) {
        try {
            jobService.applyForJob(applicantId, jobId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @Operation(summary = "Accept a job application", description = "Accepts a job application for a specific job and applicant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job application accepted"),
            @ApiResponse(responseCode = "400", description = "Job application acceptance failed")
    })
    @PostMapping("/jobs/{jobId}/accept/{applicantId}")
    public ResponseEntity<String> acceptJobApplication(@PathVariable String jobId, @PathVariable String applicantId) {
        try {
            jobService.acceptJobApplication(jobId, applicantId);
            return ResponseEntity.ok("Application accepted.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Reject a job application", description = "Rejects a job application for a specific job and applicant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job application rejected"),
            @ApiResponse(responseCode = "400", description = "Job application rejection failed")
    })
    @PostMapping("/jobs/{jobId}/reject/{applicantId}")
    public ResponseEntity<String> rejectJobApplication(@PathVariable String jobId, @PathVariable String applicantId) {
        try {
            jobService.rejectJobApplication(jobId, applicantId);
            return ResponseEntity.ok("Application rejected.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
