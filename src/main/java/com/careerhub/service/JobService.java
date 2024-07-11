package com.careerhub.service;

import com.careerhub.model.Applicants;
import com.careerhub.model.Company;
import com.careerhub.model.Job;
import com.careerhub.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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
        List<String> blockedUsersID = company.get().getBlockedUsers();

        if(blockedUsersID.contains(userId)){
            throw new RuntimeException("This user has been blocked therefore cannot apply...");
        }

        applyForJob(userId,jobId);

        return null;
    }

    public Job acceptAndDecline(String jobId, String userId, Boolean isAccepted) throws RuntimeException{
        Job job = jobRepository.findByJobId(jobId).orElseThrow();
        Company company = companyService.getCompanyById(job.getCompanyId()).orElseThrow();
        Applicants applicant = applicantService.getApplicantByUserId(userId).orElseThrow();

        List<String> applicantIds = Optional.ofNullable(job.getApplicantIds()).orElse(Collections.emptyList());
        List<String> appliedJobIds = Optional.ofNullable(applicant.getAppliedJobIds()).orElse(Collections.emptyList());

        if(applicantIds.contains(userId)){
            applicantIds.remove(userId);
            //appliedJobIds.remove(jobId);

//            if (!applicantIds.isEmpty()){
//                job.getApplicantIds().remove(userId);
//            }
//            if (!appliedJobIds.isEmpty()){
//                applicant.getAppliedJobIds().remove(jobId);
//            }

            if(isAccepted){
                String format = "Your job application for %s at %s has been successfully confirmed. Thanks for your interest!";
                String emailBody = String.format(format, job.getJobTitle(), company.getCompanyName());
                emailService.sendSimpleEmail(applicant.getEmail(), "Your Job Application results are here!", emailBody);
            }
            else {
                String format = "Your job application for %s at %s has been declined. Thanks for your interest";
                String emailBody = String.format(format, job.getJobTitle(), company.getCompanyName());
                emailService.sendSimpleEmail(applicant.getEmail(), "Your Job Application results are here!", emailBody);
            }
        }

        //applicant.setAppliedJobIds(applicantIds);
        applicantService.saveApplicant(applicant);

        return job;
    }
}
