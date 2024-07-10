package com.careerhub.service;

import com.careerhub.model.Company;
import com.careerhub.model.Job;
import com.careerhub.repository.CompanyRepository;
import com.careerhub.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ApplicantService applicantService;

    public List<Job> getJobsByCompanyId(String companyId){
        return jobRepository.findByCompanyId(companyId);
    }

    public Optional<Company> getCompanyById(String companyId) {

        return companyRepository.findByCompanyId(companyId);
    }

    public Optional<Job> getJobById(String jobId){
        return jobRepository.findByJobId(jobId);
    }

    public ResponseEntity<Job> apply(String jobId, String userId){
        Optional<Job> job = jobRepository.findByJobId(jobId);

        if(job.isEmpty()){
            throw new RuntimeException("Job not found");
        }
        String companyId = job.get().getCompanyId();
        Optional<Company> company =companyRepository.findByCompanyId(companyId);

        if(company.isEmpty()){
            throw new RuntimeException("company not found");
        }
        List<String> blockedUsersID = company.get().getBlockedUsersID();

        if(blockedUsersID.contains(userId)){
            throw new RuntimeException("This user has been blocked therefore cannot apply...");
        }

        applicantService.applyForJob(userId,jobId);

        return null;
    }
}
