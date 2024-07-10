package com.careerhub.request;

import lombok.Data;

@Data
public class CompanyLoginRequest {
    // -----------------------Company Login Credentials ----------------------------------
    private String email;
    private String password;
}
