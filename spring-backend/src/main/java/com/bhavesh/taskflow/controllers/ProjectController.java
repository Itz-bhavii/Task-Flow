package com.bhavesh.taskflow.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createProject( @RequestBody ProjectRequestDTO projectRequest) {
        boolean created = projectCreationService.createProject(projectRequest);
        if (created) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("message","Project created successfully"));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message","Project creation failed"));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<?> addMemberToProject( 
        @PathVariable("id") Long projectId,
        @RequestBody ProjectAddMemberRequestDTO request ) {

        boolean added =
                projectCreationService.addMemberToProject(
                        projectId,
                        request.getEmail()
                );
        if (added) {
            return ResponseEntity.ok(Map.of("message","Member added successfully"));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message","Failed to add member"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectDetails(@PathVariable("id") Long projectId) {
        ProjectResponseDTO project = projectCreationService.getProjectDetails(projectId);
        if (project != null) {
            return ResponseEntity.ok(project);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<ProjectResponseDTO>> getMyProjects() {
        List<ProjectResponseDTO> projects = projectCreationService.getMyProjects();
        return ResponseEntity.ok(projects);
    }
    
    
    @PostMapping("/{id}/tasks")
    public ResponseEntity<?> createTask(
        @PathVariable("id") Long projectId,
        @RequestBody TaskRequestDTO taskRequest ) {

        boolean created =
                taskCreationService.createTask(
                        projectId,
                        taskRequest
                );
        if (created) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("message","Task created successfully"));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message","Task creation failed"));
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getTasksForProject(
        @PathVariable("id") Long projectId ) {
            
    return ResponseEntity.ok(
            taskService.getTasksForProject(projectId)
    );
}

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject( @PathVariable("id") Long projectId) {

        boolean deleted = projectCreationService.deleteProject(projectId);

        if (deleted) {
            return ResponseEntity
                    .noContent()
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message","Project deletion failed"));
    }

}
