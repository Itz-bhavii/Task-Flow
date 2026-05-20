package com.bhavesh.taskflow.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhavesh.taskflow.dtos.ProjectMemberResponseDTO;
import com.bhavesh.taskflow.enums.ProjectRole;
import com.bhavesh.taskflow.models.Project;
import com.bhavesh.taskflow.models.ProjectMember;
import com.bhavesh.taskflow.models.User;
import com.bhavesh.taskflow.repository.ProjectMemberRepository;

@Service
public class ProjectMemberService {
    
    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    public boolean addMemberToProject(Project project, User user) {
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setUser(user);
        if(project.getCreatedBy().getId().equals(user.getId())) {
            projectMember.setRole(ProjectRole.ADMIN);
        } else {
            projectMember.setRole(ProjectRole.MEMBER);
        }
        try{
            return projectMemberRepository.save(projectMember) != null;
        } catch (Exception e) {
            throw new RuntimeException("Failed To add Maybe Member already exists in the project");
        }
    }

    public boolean isUserProjectMember(Long projectId, Long userId) {
        return projectMemberRepository.existsByProjectIdAndUserId(projectId, userId);
    }

    public ProjectRole getUserProjectRole(Long projectId, Long userId) {
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndUserId(projectId, userId);
        return projectMember != null ? projectMember.getRole() : null;
    }

    public List<Project> findProjectsOfCurrentlyLoggedUser(){
        User currentUser = UserService.getCurrentAuthenticatedUser();
        List<ProjectMember> projectMembers= projectMemberRepository.findByUserId(currentUser.getId());
        List<Project> projects = new ArrayList<>();
        for (ProjectMember pm : projectMembers){
            projects.add(pm.getProject());
        }
        return projects;
    }

    public boolean deleteProjectMembersByProjectId(Long projectId) {
        projectMemberRepository.deleteByProjectId(projectId);
        return projectMemberRepository.existsByProjectId(projectId);
    }

    public List<ProjectMemberResponseDTO> getProjectMembers(Long projectId) {
        List<ProjectMember> projectMembers = projectMemberRepository.findByProjectId(projectId);
        List<ProjectMemberResponseDTO> memberResponseDTOs = new ArrayList<>();
        for (ProjectMember pm : projectMembers) {
            ProjectMemberResponseDTO dto = new ProjectMemberResponseDTO();
            dto.setUsername(pm.getUser().getUsername());
            dto.setEmail(pm.getUser().getEmail());
            dto.setRole(pm.getRole());
            memberResponseDTOs.add(dto);
        }
        return memberResponseDTOs;
    }


}
