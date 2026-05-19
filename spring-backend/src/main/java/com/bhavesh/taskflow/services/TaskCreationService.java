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
            return false; 
        }

        Project project = projectService.getProjectById(projectId);
        User assignedTo = userService.getUserByEmail(taskRequest.getAssignedToEmail());
        return taskService.createTask(taskRequest, project, assignedTo);

    }

    public boolean validityChecks(Long projectId, TaskRequestDTO taskRequest) {
        if(!validateTaskInput(taskRequest)) {
            return false;
        }
        if (!projectService.projectExists(projectId)) {
            return false;
        }
        if(!taskRequest.getAssignedToEmail().isEmpty()){
            if(!userService.userExistsByEmail(taskRequest.getAssignedToEmail())) {
                return false; 
            }
            if(!projectMemberService.isUserProjectMember(projectId, userService.getUserByEmail(taskRequest.getAssignedToEmail()).getId())) {
                return false;
            }
        }
        if(!UserService.getCurrentAuthenticatedUser().getId().equals(projectService.getProjectById(projectId).getCreatedBy().getId())) {
                return false;
        }  
        
        
        return true;
    }

    public boolean validateTaskInput(TaskRequestDTO taskRequest) {
        if (taskRequest.getTitle() == null || taskRequest.getTitle().isEmpty()) {
            return false; 
        }
        return true; 
    }
}
