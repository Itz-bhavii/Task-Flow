package com.bhavesh.taskflow.dtos;

import java.time.LocalDateTime;

import com.bhavesh.taskflow.enums.TaskStatus;

import lombok.Data;

@Data
public class TaskResponseDTO {
    private Long id;
    private String taskTitle;
    private String taskDescription;
    private TaskStatus taskStatus;
    private LocalDateTime dueDate;
    private String projectName;
    private String assignedToEmail;
    private String assignedToUsername;
    private String createdBy;

}
