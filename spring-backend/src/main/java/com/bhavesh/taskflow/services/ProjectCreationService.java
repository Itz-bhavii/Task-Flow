package com.bhavesh.taskflow.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhavesh.taskflow.dtos.ProjectRequestDTO;
import com.bhavesh.taskflow.dtos.ProjectResponseDTO;
import com.bhavesh.taskflow.models.Project;
import com.bhavesh.taskflow.models.User;

import jakarta.transaction.Transactional;

@Service
public class ProjectCreationService {
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private UserService userService;


    @Transactional
    public boolean createProject(ProjectRequestDTO projectRequest) {

        Project project = projectService.createProject(projectRequest);
        if(project == null) {
            throw new RuntimeException("Project creation failed");
        }
        return projectMemberService.addMemberToProject(project, project.getCreatedBy());
    }

    public boolean addMemberToProject(Long projectId, String email) {
        if(email == null || email.trim().length() == 0) {
            throw new RuntimeException("Email is required");
        }

        Project project = projectService.getProjectById(projectId);

        if(UserService.getCurrentAuthenticatedUser().getId() != project.getCreatedBy().getId()) {
            throw new RuntimeException("You are not the owner of this project");
        }

        User user = userService.getUserByEmail(email);
        if(user == null || project == null) {
            throw new RuntimeException("User or Project not found");
        }
        return projectMemberService.addMemberToProject(project, user);
    }

    public ProjectResponseDTO getProjectDetails(Long projectId) {
        Project project = projectService.getProjectById(projectId);
        if(project == null) {
            throw new RuntimeException("Project not found");
        }
        if(!projectMemberService.isUserProjectMember(projectId, UserService.getCurrentAuthenticatedUser().getId())) {
            throw new RuntimeException("You are not a member of this project");
        }
        
        return projectService.convertProjectToDTO(project);
    }

    public List<ProjectResponseDTO> getMyProjects() {
        List<Project> projects = projectMemberService.findProjectsOfCurrentlyLoggedUser();
        List<ProjectResponseDTO> projectDTOs = new ArrayList<>();
        for(Project project : projects) {
            projectDTOs.add(projectService.convertProjectToDTO(project));
        }
        return projectDTOs;
    }

    public boolean deleteProject(Long projectId) {
        Project project = projectService.getProjectById(projectId);
        if(project == null) {
            throw new RuntimeException("Project not found");
        }
        if(UserService.getCurrentAuthenticatedUser().getId() != project.getCreatedBy().getId()) {
            throw new RuntimeException("You are not the owner of this project");
        }
        projectService.deleteProject(projectId);
        return true;
    }
}
