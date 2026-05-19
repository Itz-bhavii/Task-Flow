package com.bhavesh.taskflow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhavesh.taskflow.dtos.TaskRequestDTO;
import com.bhavesh.taskflow.models.Project;
import com.bhavesh.taskflow.models.User;

@Service
public class TaskCreationService {
    
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectMemberService projectMemberService;


    public boolean createTask(Long projectId, TaskRequestDTO taskRequest) {
        if (!validityChecks(projectId, taskRequest)) {
            throw new RuntimeException("Invalid task creation request");
        }

        Project project = projectService.getProjectById(projectId);
        User assignedTo = userService.getUserByEmail(taskRequest.getAssignedToEmail());
        return taskService.createTask(taskRequest, project, assignedTo);

    }

    public boolean validityChecks(Long projectId, TaskRequestDTO taskRequest) {
        if(!validateTaskInput(taskRequest)) {
            throw new RuntimeException("Invalid task input");
        }
        if (!projectService.projectExists(projectId)) {
            throw new RuntimeException("Project not found");
        }
        if(!taskRequest.getAssignedToEmail().isEmpty()){
            if(!userService.userExistsByEmail(taskRequest.getAssignedToEmail())) {
                throw new RuntimeException("User not found");
            }
            if(!projectMemberService.isUserProjectMember(projectId, userService.getUserByEmail(taskRequest.getAssignedToEmail()).getId())) {
                throw new RuntimeException("User is not a member of this project");
            }
        }
        if(!UserService.getCurrentAuthenticatedUser().getId().equals(projectService.getProjectById(projectId).getCreatedBy().getId())) {
                throw new RuntimeException("You are not the owner of this project");
        }  
        
        
        return true;
    }

    public boolean validateTaskInput(TaskRequestDTO taskRequest) {
        if (taskRequest.getTitle() == null || taskRequest.getTitle().isEmpty()) {
            throw new RuntimeException("Task title is required");
        }
        return true; 
    }
}
