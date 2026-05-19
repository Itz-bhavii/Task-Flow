package com.bhavesh.taskflow.dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaskRequestDTO {
    private String title;
    private String description;
    private String assignedToEmail;
    private LocalDateTime dueDate;
}
