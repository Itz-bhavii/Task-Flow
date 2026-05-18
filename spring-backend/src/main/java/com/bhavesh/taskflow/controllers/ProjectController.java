package com.bhavesh.taskflow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhavesh.taskflow.dtos.ProjectRequestDTO;
import com.bhavesh.taskflow.services.ProjectCreationService;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    
    @Autowired
    private ProjectCreationService projectCreationService;

    @PostMapping
    public boolean createProject(@RequestBody ProjectRequestDTO projectRequest) {
        return projectCreationService.createProject(projectRequest);
    }
}
