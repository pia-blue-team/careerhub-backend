package com.careerhub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("jobs")
public class Job {

    private String jobId;
    private String jobTitle;
    private LocalDate applicationBeginningDate;
    private LocalDate applicationDeadline;
    private String position;
    private String jobDescription;
    private String companyId;
    //    @DBRef
//    private Company company;

}
