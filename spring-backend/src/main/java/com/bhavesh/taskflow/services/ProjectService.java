package com.bhavesh.taskflow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhavesh.taskflow.dtos.ProjectRequestDTO;
import com.bhavesh.taskflow.models.Project;
import com.bhavesh.taskflow.models.User;
import com.bhavesh.taskflow.repository.ProjectRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;


    public Project createProject(ProjectRequestDTO projectRequest) {
        if(projectRequest.getName() == null || projectRequest.getName().trim().length() < 3) {
            return null;
        }
        if(projectRepository.existsByName(projectRequest.getName())) {
            return null;
        }
        return saveProject(projectRequest);
    }

    public Project saveProject(ProjectRequestDTO projectRequest) {
        User currentUser = UserService.getCurrentAuthenticatedUser();
        Project project = new Project();
        project.setName(projectRequest.getName());
        project.setDescription(projectRequest.getDescription());
        project.setCreatedBy(currentUser);
        return projectRepository.save(project);
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }
    
    
}
