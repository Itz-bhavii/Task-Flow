package com.bhavesh.taskflow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhavesh.taskflow.dtos.ProjectRequestDTO;
import com.bhavesh.taskflow.models.Project;

import jakarta.transaction.Transactional;

@Service
public class ProjectCreationService {
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private ProjectMemberService projectMemberService;

    @Transactional
    public boolean createProject(ProjectRequestDTO projectRequest) {
        Project project = projectService.createProject(projectRequest);
        if(project == null) {
            return false;
        }
        return projectMemberService.addMemberToProject(project, project.getCreatedBy());
    }
}
