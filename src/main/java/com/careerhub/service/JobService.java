package com.careerhub.service;

import com.careerhub.model.Company;
import com.careerhub.model.Job;
import com.careerhub.repository.CompanyRepository;
import com.careerhub.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    public List<Job> getJobsByCompanyId(String companyId){
        return jobRepository.findByCompanyId(companyId);
    }

    public Optional<Company> getCompanyById(String companyId) {

        return companyRepository.findByCompanyId(companyId);
    }

    public Optional<Job> getJobById(String jobId){
        return jobRepository.findByJobId(jobId);
    }

    public Job createJob(Job job) {
        Job savedJob = jobRepository.save(job);

        Optional<Company> companyOptional = companyRepository.findByCompanyId(job.getCompanyId());
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            company.getJobIds().add(savedJob.getJobId());
        }
        return savedJob;
    }

    public Job addApplicantToJob(String jobId, String applicantId) {
        Optional<Job> jobOptional = jobRepository.findByJobId(jobId);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.getApplicantIds().add(applicantId);
            return jobRepository.save(job);
        }
        return null;
    }

    public List<String> getApplicantsByJobId(String jobId) {
        Optional<Job> jobOptional = jobRepository.findByJobId(jobId);
        return jobOptional.map(Job::getApplicantIds).orElse(null);
    }
}
