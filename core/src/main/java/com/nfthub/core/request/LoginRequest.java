package com.nfthub.core.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank(message = "Username is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
}
