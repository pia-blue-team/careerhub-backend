package com.careerhub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("companies")
public class Company {
    private String companyId;
    private String companyName;
    private String field;
    private String location;
    private String companyDescription;
}
