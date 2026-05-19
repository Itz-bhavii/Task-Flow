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
            return false;
        }
        return projectMemberService.addMemberToProject(project, project.getCreatedBy());
    }

    public boolean addMemberToProject(Long projectId, String email) {
        if(email == null || email.trim().length() == 0) {
            return false;
        }

        Project project = projectService.getProjectById(projectId);

        if(UserService.getCurrentAuthenticatedUser().getId() != project.getCreatedBy().getId()) {
                return false;
        }

        User user = userService.getUserByEmail(email);
        if(user == null || project == null) {
            return false;
        }
        return projectMemberService.addMemberToProject(project, user);
    }

    public ProjectResponseDTO getProjectDetails(Long projectId) {
        Project project = projectService.getProjectById(projectId);
        if(project == null) {
            return null;
        }
        if(!projectMemberService.isUserProjectMember(projectId, UserService.getCurrentAuthenticatedUser().getId())) {
            return null;
        }
        
        return projectService.convertProjectToDTO(project);
    }

    public List<ProjectResponseDTO> getMyProjects() {
        List<Project> projects = projectService.getProjectsByUserId(UserService.getCurrentAuthenticatedUser().getId());
        List<ProjectResponseDTO> projectDTOs = new ArrayList<>();
        for(Project project : projects) {
            projectDTOs.add(projectService.convertProjectToDTO(project));
        }
        return projectDTOs;
    }
}
