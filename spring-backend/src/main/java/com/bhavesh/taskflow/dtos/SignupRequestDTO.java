package com.bhavesh.taskflow.dtos;

import lombok.Data;

@Data
public class SignupRequestDTO {
    private String username;
    private String email;
    private String password;
}
