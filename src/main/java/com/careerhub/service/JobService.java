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
}
