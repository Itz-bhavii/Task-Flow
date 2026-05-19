package com.bhavesh.taskflow.dtos;

import java.time.LocalDateTime;

import com.bhavesh.taskflow.enums.TaskStatus;

import lombok.Data;

@Data
public class TaskRequestDTO {
    private String title;
    private String description;
    private String assignedToEmail;
    private LocalDateTime dueDate;
    private TaskStatus taskStatus;
}
