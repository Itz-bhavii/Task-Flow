package com.bhavesh.taskflow.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhavesh.taskflow.dtos.ProjectRequestDTO;
import com.bhavesh.taskflow.dtos.ProjectResponseDTO;
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

    public boolean projectExists(Long projectId) {
        return projectRepository.existsById(projectId);
    }


    public List<Project> getProjectsByUserId(Long userId) {
        return projectRepository.findByCreatedById(userId);
    }
    

    public ProjectResponseDTO convertProjectToDTO(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setProjectName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setCreatedByUsername(project.getCreatedBy().getName());
        dto.setCreatedByEmail(project.getCreatedBy().getEmail());
        return dto;
    }
    
    
}
