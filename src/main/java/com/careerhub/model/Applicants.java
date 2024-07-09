package com.careerhub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("Applicants")
public class Applicants extends User{

    private String cvPath;
    private String aboutUser;
    private String currentRole;
    private List<String> appliedJobIds = new ArrayList<>(); // personal list for jobs that have been applied by the applicant.


    public Applicants(String name, String surname, String email, String password, String cvPath, String aboutUser, String currentRole) {
        super(name, surname, email, password);
        this.cvPath = cvPath;
        this.aboutUser = aboutUser;
        this.currentRole = currentRole;
    }
}
