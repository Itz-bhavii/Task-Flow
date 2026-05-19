package com.bhavesh.taskflow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhavesh.taskflow.dtos.ProjectAddMemberRequestDTO;
import com.bhavesh.taskflow.dtos.ProjectRequestDTO;
import com.bhavesh.taskflow.dtos.TaskRequestDTO;
import com.bhavesh.taskflow.services.ProjectCreationService;
import com.bhavesh.taskflow.services.TaskCreationService;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    
    @Autowired
    private ProjectCreationService projectCreationService;

    @Autowired
    private TaskCreationService taskCreationService;

    @PostMapping
    public boolean createProject(@RequestBody ProjectRequestDTO projectRequest) {
        return projectCreationService.createProject(projectRequest);
    }

    @PostMapping("/{id}/members")
    public boolean addMemberToProject(@PathVariable("id") Long projectId, @RequestBody ProjectAddMemberRequestDTO request) {
        return projectCreationService.addMemberToProject(projectId, request.getEmail());
    }
    
    
    @PatchMapping("/{id}/tasks")
    public boolean createTask(@PathVariable("id") Long projectId, @RequestBody TaskRequestDTO taskRequest) {
        return taskCreationService.createTask(projectId, taskRequest);
    }

}
