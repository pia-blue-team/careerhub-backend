package com.careerhub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomCompanyJobsResponse {
    private Company company;
    private List<Job> jobs;
}
