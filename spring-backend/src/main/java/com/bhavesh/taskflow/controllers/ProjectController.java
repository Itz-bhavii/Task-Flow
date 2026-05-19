package com.bhavesh.taskflow.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhavesh.taskflow.dtos.ProjectAddMemberRequestDTO;
import com.bhavesh.taskflow.dtos.ProjectRequestDTO;
import com.bhavesh.taskflow.dtos.ProjectResponseDTO;
import com.bhavesh.taskflow.dtos.TaskRequestDTO;
import com.bhavesh.taskflow.dtos.TaskResponseDTO;
import com.bhavesh.taskflow.services.ProjectCreationService;
import com.bhavesh.taskflow.services.TaskCreationService;
import com.bhavesh.taskflow.services.TaskService;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    
    @Autowired
    private ProjectCreationService projectCreationService;

    @Autowired
    private TaskCreationService taskCreationService;

    @Autowired
    private TaskService taskService;

    @PostMapping
    public boolean createProject(@RequestBody ProjectRequestDTO projectRequest) {
        return projectCreationService.createProject(projectRequest);
    }

    @PostMapping("/{id}/members")
    public boolean addMemberToProject(@PathVariable("id") Long projectId, @RequestBody ProjectAddMemberRequestDTO request) {
        return projectCreationService.addMemberToProject(projectId, request.getEmail());
    }

    @GetMapping("/{id}")
    public ProjectResponseDTO getProjectDetails(@PathVariable("id") Long projectId) {
        return projectCreationService.getProjectDetails(projectId);
    }

    @GetMapping("/my")
    public List<ProjectResponseDTO> getMyProjects() {
        return projectCreationService.getMyProjects();
    }
    
    
    @PostMapping("/{id}/tasks")
    public boolean createTask(@PathVariable("id") Long projectId, @RequestBody TaskRequestDTO taskRequest) {
        return taskCreationService.createTask(projectId, taskRequest);
    }

    @GetMapping("/{id}/tasks")
    public List<TaskResponseDTO> getTasksForProject(@PathVariable("id") Long projectId) {
        return taskService.getTasksForProject(projectId);
    }

    @DeleteMapping("/{id}")
    public boolean deleteProject(@PathVariable("id") Long projectId) {
        return projectCreationService.deleteProject(projectId);
    }

}
