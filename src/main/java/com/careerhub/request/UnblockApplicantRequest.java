package com.careerhub.request;

import lombok.Data;

@Data
public class UnblockApplicantRequest {
    private String companyId;
    private String applicantEmail;
}
