package com.bhavesh.taskflow.dtos;

import com.bhavesh.taskflow.enums.ProjectRole;

import lombok.Data;

@Data
public class ProjectMemberResponseDTO {
    private String username;
    private String email;
    private ProjectRole role;
}
