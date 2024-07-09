package com.careerhub.service;

import com.careerhub.model.Company;
import com.careerhub.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MailService {
    @Autowired
    private JobService jobService;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendJobApplicationEmail(String to, String jobId){
        SimpleMailMessage message = new SimpleMailMessage();
        Optional<Job> jobOpt = jobService.getJobById(jobId);
        if (jobOpt.isEmpty()){
            System.out.println("Unsuccessful");
        }
        Job job = jobOpt.get();
        String companyId = job.getCompanyId();
        Company company = jobService.getCompanyById(companyId).get();
        message.setTo(to);
        message.setSubject("Job Application Successful");
        message.setText("Your job application for  " + job.getJobTitle() + " at " + company.getCompanyName() + " has been successfully submitted. Thanks for your interest!");
        javaMailSender.send(message);
        System.out.println("Successful!");
    }
}
