package com.careerhub.service;

import com.careerhub.controller.EmailController;
import com.careerhub.model.Applicants;
import com.careerhub.model.Company;
import com.careerhub.model.Job;
import com.careerhub.repository.JobRepository;
import com.careerhub.status.ApplicantStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ApplicantService applicantService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailController email;

    public List<Job> getJobsByCompanyId(String companyId){
        return jobRepository.findByCompanyId(companyId);
    }

    public Optional<Job> getJobById(String jobId){
        return jobRepository.findByJobId(jobId);
    }

    public Job createJob(Job job) {
        Job savedJob = jobRepository.save(job);

        Optional<Company> companyOptional = companyService.getCompanyById(job.getCompanyId());
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            List<String> jobIds = Optional.of(company.getJobIds()).orElse(Collections.emptyList());
            jobIds.add(savedJob.getJobId());
            company.setJobIds(jobIds);
        }
        return savedJob;
    }
    public void deleteJob(String companyId, String jobId) {
        Optional<Job> jobOptional = jobRepository.findByJobId(jobId);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();

            if (!job.getCompanyId().equals(companyId)) {
                throw new RuntimeException("You are not authorized to delete this job.");
            }

            jobRepository.delete(job);
        } else {
            throw new RuntimeException("Job not found with id: " + jobId);
        }
    }

    public void addApplicantToJob(String jobId, String applicantId) {
        Optional<Job> jobOptional = jobRepository.findByJobId(jobId);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.getApplicantIds().add(applicantId);
            jobRepository.save(job);
        }
    }

    public List<String> getApplicantsByJobId(String jobId) {
        Optional<Job> jobOptional = jobRepository.findByJobId(jobId);
        return jobOptional.map(Job::getApplicantIds).orElse(null);
    }

    public void applyForJob(String applicantId, String jobId) throws RuntimeException {
        Applicants applicant = applicantService.getApplicantByUserId(applicantId).orElseThrow();
        Job job= getJobById(jobId).orElseThrow();
        String companyId = job.getCompanyId();
        Company company = companyService.getCompanyById(companyId).orElseThrow();

        if (companyId == null)
            throw new RuntimeException("Unexpected error: Company ID is empty.");

        if (companyService.isUserBlacklistedByCompany(companyId, applicantId))
            throw new RuntimeException("You are not allowed to apply for this company since you are blocked.");

        if (applicant.getAppliedJobIds().contains(jobId))
            throw new RuntimeException("You have already applied for this job.");

        List<String> jobIds = Optional.of(applicant.getAppliedJobIds()).orElse(Collections.emptyList());
        jobIds.add(jobId);
        applicant.setAppliedJobIds(jobIds);
        applicantService.saveApplicant(applicant);

        String format = "Your job application for %s at %s has been successfully submitted. Thanks for your interest!";
        String emailBody = String.format(format, job.getJobTitle(), company.getCompanyName());
        emailService.sendSimpleEmail(applicant.getEmail(), "Job Application Successful", emailBody);
    }

    public List<Job> getAppliedJobs(String userId) {
        Optional<Applicants> applicantOpt = applicantService.getApplicantByUserId(userId);
        if (applicantOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Applicants applicant = applicantOpt.get();
        List<String> appliedJobIds = applicant.getAppliedJobIds();
        List<Job> jobs = new ArrayList<>();
        for (String jobId : appliedJobIds) {
            Optional<Job> jobOpt = jobRepository.findByJobId(jobId);
            if (jobOpt.isEmpty()) {
                throw new RuntimeException("Job not found");
            }
            jobs.add(jobOpt.get());
        }
        return jobs;
    }

    public ResponseEntity<Job> apply(String jobId, String userId){
        Optional<Job> job = jobRepository.findByJobId(jobId);

        if(job.isEmpty()){
            throw new RuntimeException("Job not found");
        }
        String companyId = job.get().getCompanyId();
        Optional<Company> company = companyService.getCompanyById(companyId);

        if(company.isEmpty()){
            throw new RuntimeException("company not found");
        }
        List<String> blockedUsersID = company.get().getBlacklistedUsers();

        if(blockedUsersID.contains(userId)){
            throw new RuntimeException("This user has been blocked therefore cannot apply...");
        }

        applyForJob(userId,jobId);

        return null;
    }
    public void acceptApplicant(String jobId, String applicantId) {
        Optional<Job> jobOptional = jobRepository.findByJobId(jobId);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            List<ApplicantStatus> statuses = job.getApplicantStatuses();
            if (statuses == null) {
                statuses = new ArrayList<>();
            }
            for (ApplicantStatus status : statuses) {
                if (status.getApplicantId().equals(applicantId)) {
                    status.setStatus("ACCEPTED");
                    job.setApplicantStatuses(statuses);
                    jobRepository.save(job);
                    return;
                }
            }
            statuses.add(new ApplicantStatus(applicantId, "ACCEPTED"));
            job.setApplicantStatuses(statuses);
            jobRepository.save(job);
        }
    }

    public void rejectApplicant(String jobId, String applicantId) {
        Optional<Job> jobOptional = jobRepository.findByJobId(jobId);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            List<ApplicantStatus> statuses = job.getApplicantStatuses();
            if (statuses == null) {
                statuses = new ArrayList<>();
            }
            for (ApplicantStatus status : statuses) {
                if (status.getApplicantId().equals(applicantId)) {
                    status.setStatus("REJECTED");
                    job.setApplicantStatuses(statuses);
                    jobRepository.save(job);
                    return;
                }
            }
            statuses.add(new ApplicantStatus(applicantId, "REJECTED"));
            job.setApplicantStatuses(statuses);
            jobRepository.save(job);
        }
    }
}
