package com.bhavesh.taskflow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhavesh.taskflow.enums.ProjectRole;
import com.bhavesh.taskflow.models.Project;
import com.bhavesh.taskflow.models.ProjectMember;
import com.bhavesh.taskflow.models.User;
import com.bhavesh.taskflow.repository.ProductMemberRepository;

@Service
public class ProjectMemberService {
    
    @Autowired
    private ProductMemberRepository productMemberRepository;

    public boolean addMemberToProject(Project project, User user) {
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setUser(user);
        if(project.getCreatedBy().getId().equals(user.getId())) {
            projectMember.setRole(ProjectRole.ADMIN);
        } else {
            projectMember.setRole(ProjectRole.MEMBER);
        }
        return productMemberRepository.save(projectMember) != null;
    }

    public boolean isUserProjectMember(Long projectId, Long userId) {
        return productMemberRepository.existsByProjectIdAndUserId(projectId, userId);
    }

    public ProjectRole getUserProjectRole(Long projectId, Long userId) {
        ProjectMember projectMember = productMemberRepository.findByProjectIdAndUserId(projectId, userId);
        return projectMember != null ? projectMember.getRole() : null;
    }
}
