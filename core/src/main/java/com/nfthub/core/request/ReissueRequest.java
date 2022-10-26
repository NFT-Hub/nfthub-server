package com.nfthub.core.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReissueRequest {
    // 만료된 엑세스 토큰
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
