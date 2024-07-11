package com.careerhub.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("jobs")
public class Job {
    @Id
    private String id;
    private String jobId;
    private String jobTitle;
    private LocalDate applicationBeginningDate;
    private LocalDate applicationDeadline;
    private String position;

    @NotBlank
    private String jobDescription;

    @NotBlank
    private String companyId;
    private String location;
    private List<String> applicantIds;
}
