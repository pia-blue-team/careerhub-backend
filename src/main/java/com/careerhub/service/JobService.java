package com.careerhub.service;

import com.careerhub.model.Applicants;
import com.careerhub.model.Company;
import com.careerhub.model.Job;
import com.careerhub.repository.CompanyRepository;
import com.careerhub.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
            company.getJobIds().add(savedJob.getJobId());
        }
        return savedJob;
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

    public void applyForJob(String userId, String jobId) throws RuntimeException {
        Applicants applicant = applicantService.getApplicantByUserId(userId).orElseThrow();
        Job job = jobRepository.findByJobId(jobId).orElseThrow();
        Company company = companyService.getCompanyById(job.getCompanyId()).orElseThrow();

        if (applicant.getAppliedJobIds().contains(jobId)) throw new RuntimeException("User already applied");

        applicant.getAppliedJobIds().add(jobId);
        addApplicantToJob(jobId, applicant.getUserId());
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
            if (!jobOpt.isPresent()) {
                throw new RuntimeException("Job not found");
            }
            jobs.add(jobOpt.get());
        }
        return jobs;
    }
}
