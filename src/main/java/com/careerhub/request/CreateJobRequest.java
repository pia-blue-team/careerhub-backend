package com.careerhub.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateJobRequest {
    @NotBlank(message = "Job title cannot be blank")
    private String jobTitle;

    @NotBlank(message = "Position cannot be blank")
    private String position;

    @NotBlank(message = "Job description cannot be blank")
    @Size(min = 10, message = "Job description must be at least 15 characters long")
    private String jobDescription;

    @NotBlank(message = "Company ID cannot be blank")
    private String companyId;

    @NotBlank(message = "Location cannot be blank")
    private String location;

    private List<String> applicantIds;


}
