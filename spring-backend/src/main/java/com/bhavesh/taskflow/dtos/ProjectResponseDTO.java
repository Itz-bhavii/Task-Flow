package com.bhavesh.taskflow.dtos;

import lombok.Data;

@Data
public class ProjectResponseDTO {
    private Long id;
    private String projectName;
    private String description;
    private String createdByUsername;
    private String createdByEmail;

}
