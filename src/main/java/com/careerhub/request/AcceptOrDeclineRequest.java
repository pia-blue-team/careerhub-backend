package com.careerhub.request;

import lombok.Data;

@Data
public class AcceptOrDeclineRequest {
    private String jobId;
    private String userId;
    private Boolean isAccepted;
}
