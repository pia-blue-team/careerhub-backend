package com.careerhub.controller;

import com.careerhub.model.Company;
import com.careerhub.model.CustomCompanyJobsResponse;
import com.careerhub.model.Job;
import com.careerhub.service.CompanyService;
import com.careerhub.request.JobRequest;
import com.careerhub.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/careerhub")
public class JobController {
    @Autowired
    private JobService jobService;
    @Autowired
    private CompanyService companyService;

    @PostMapping("/createJob")
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job createdJob = jobService.createJob(job);
        return ResponseEntity.ok(createdJob);
    }

    @DeleteMapping("/deleteJob/{companyId}/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable String companyId, @PathVariable String jobId) {
        try {
            jobService.deleteJob(companyId, jobId);
            return ResponseEntity.ok("Job deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/jobs/{jobId}/applicants")
    public ResponseEntity<List<String>> getApplicantsByJobId(@PathVariable String jobId) {
        List<String> applicants = jobService.getApplicantsByJobId(jobId);
        if (applicants != null) {
            return ResponseEntity.ok(applicants);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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

    @GetMapping("/jobdetails/{jobId}")
    public ResponseEntity<Job> getJobByJobId(@PathVariable String jobId){
        Optional<Job> jobOpt = jobService.getJobById(jobId);
        if (jobOpt.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Job job = jobOpt.get();
        return ResponseEntity.ok(job);
    }

    @GetMapping("/{userId}/getAppliedJobs")
    public ResponseEntity<List<Job>> getAppliedJobs(@PathVariable String userId) {
        try {
            List<Job> appliedJobs = jobService.getAppliedJobs(userId);
            return ResponseEntity.ok(appliedJobs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/apply-check")
    public Job applyAndCheckBlocked(@RequestBody JobRequest jobRequest){
        return jobService.apply(jobRequest.getJobId(), jobRequest.getUserId()).getBody();
    }

    @PostMapping("/apply/{applicantId}/{jobId}")
    public ResponseEntity<String> applyForJob(@PathVariable String applicantId, @PathVariable String jobId) {
        try {
            jobService.applyForJob(applicantId, jobId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    @PostMapping("/jobs/{jobId}/accept/{applicantId}")
    public ResponseEntity<String> acceptApplicant(@PathVariable String jobId, @PathVariable String applicantId) {
        try {
            jobService.acceptApplicant(jobId, applicantId);
            return ResponseEntity.ok("Applicant accepted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/jobs/{jobId}/reject/{applicantId}")
    public ResponseEntity<String> rejectApplicant(@PathVariable String jobId, @PathVariable String applicantId) {
        try {
            jobService.rejectApplicant(jobId, applicantId);
            return ResponseEntity.ok("Applicant rejected successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
