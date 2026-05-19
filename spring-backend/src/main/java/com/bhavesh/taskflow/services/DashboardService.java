package com.bhavesh.taskflow.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhavesh.taskflow.dtos.DashboardResponseDTO;
import com.bhavesh.taskflow.models.Project;
import com.bhavesh.taskflow.models.Task;

@Service
public class DashboardService {
    
    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectMemberService projectMemberService;

    public DashboardResponseDTO getCurrentUserData(){
        int assignedTasksCount = getAssignedTasksCount();
        int completedTasksCount = getCompletedTaskCount();
        int pendingTasksCount = getPendingTaskCount();
        int overdueTasksCount = getOverdueTaskCount();
        int projectCount = getProjectCounts();
        return DashboardResponseDTO.builder()
                .assignedTasks(assignedTasksCount)
                .completedTasks(completedTasksCount)
                .pendingTasks(pendingTasksCount)
                .overdueTasks(overdueTasksCount)
                .projectCounts(projectCount)
                .build();
    }

    public int getAssignedTasksCount(){
        List<Task> tasks= taskService.getCurrentUserAssignedTasks();        
        return tasks.size();
    }

    public int getCompletedTaskCount(){
        List<Task> tasks= taskService.getCurrentUserCompletedTasks(); 
        return tasks.size(); 
    }

    public int getPendingTaskCount(){
        List<Task> tasks = taskService.getCurrentUserPendingTasks();
        return tasks.size();
    }

    public int getOverdueTaskCount(){
        List<Task> tasks = taskService.getCurrentUserOverdueTasks();
        return tasks.size();
    }

    public int getProjectCounts(){
        List<Project> projects = projectMemberService.findProjectsOfCurrentlyLoggedUser();
        return projects.size();
    }



}
