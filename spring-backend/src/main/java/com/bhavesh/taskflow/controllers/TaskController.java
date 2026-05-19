package com.bhavesh.taskflow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhavesh.taskflow.dtos.TaskRequestDTO;
import com.bhavesh.taskflow.dtos.TaskResponseDTO;
import com.bhavesh.taskflow.services.TaskService;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @GetMapping("/{id}")
    public TaskResponseDTO getTask(@PathVariable("id") Long taskId) {
        return taskService.getTaskById(taskId);
    }

    @PatchMapping("/{id}")
    public boolean updateTask(@PathVariable("id") Long taskId, @RequestBody TaskRequestDTO Taskrequest) {
        return taskService.updateTask(taskId, Taskrequest);
    }
}
