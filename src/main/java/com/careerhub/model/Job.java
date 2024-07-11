package com.careerhub.model;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("jobs")
public class Job {
    @Id
    private String id;
    private String jobId;

    @NotBlank(message = "Job title cannot be blank")
    private String jobTitle;

    @FutureOrPresent(message = "Application beginning date must be in the future or present")
    private LocalDate applicationBeginningDate;

    @FutureOrPresent(message = "Application deadline must be in the future or present")
    private LocalDate applicationDeadline;

    @NotBlank(message = "Position cannot be blank")
    private String position;

    @NotBlank(message = "Job description cannot be blank")
    @Size(min = 10, message = "Job description must be at least 15 characters long")
    private String jobDescription;

    private String companyId;

    @NotBlank(message = "Location cannot be blank")
    private String location;


    private List<String> applicantIds;
}
