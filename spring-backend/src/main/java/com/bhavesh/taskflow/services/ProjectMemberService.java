package com.bhavesh.taskflow.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return projectMemberRepository.save(projectMember) != null;
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


}
