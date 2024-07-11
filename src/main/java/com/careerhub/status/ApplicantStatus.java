package com.careerhub.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ApplicantStatus {
        private String applicantId;
        private String status; // PENDING, ACCEPTED, REJECTED gibi değerler alabilir
    }
