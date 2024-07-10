package com.careerhub.service;

import com.careerhub.model.Applicants;
import com.careerhub.repository.ApplicantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApplicantService {

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private FileStorageService fileStorageService;

    public Optional<Applicants> getApplicantByUserId(String id) {
        return applicantRepository.findByUserId(id);
    }

    public Applicants saveApplicantWithCv(String firstName, String lastName, String password, String email, String aboutUser, String currentRole, MultipartFile cvFile) throws IOException {
        // Store the file and get the relative path
        String cvPath = fileStorageService.storeFile(cvFile, firstName, lastName);

        // Create and save user
        Applicants applicants = new Applicants(firstName, lastName, email, password, cvPath, aboutUser, currentRole);
        applicants.setUserId(UUID.randomUUID().toString());
//        user.setName(firstName);
//        user.setSurname(lastName);
//        user.setPassword(password);
//        user.setEmail(email);
//        user.setCvPath(cvPath);

        return applicantRepository.save(applicants);
    }

    public Applicants getApplicantByEmail(String email) {
        return applicantRepository.findByEmail(email);
    }

    public Applicants applyForJob(String userId, String jobId) {
        Optional<Applicants> ApplicantOptional = applicantRepository.findById(userId);
        if (ApplicantOptional.isPresent()) {
            Applicants applicant = ApplicantOptional.get();
            if (!applicant.getAppliedJobIds().contains(jobId)) {
                applicant.getAppliedJobIds().add(jobId);
                applicantRepository.save(applicant);

                mailService.sendJobApplicationEmail(applicant.getEmail(), jobId);
            }
            return applicant;
        }
        return null;
    }
}
