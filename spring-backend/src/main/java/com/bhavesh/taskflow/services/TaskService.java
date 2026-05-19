package com.bhavesh.taskflow.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhavesh.taskflow.dtos.TaskRequestDTO;
import com.bhavesh.taskflow.dtos.TaskResponseDTO;
import com.bhavesh.taskflow.enums.TaskStatus;
import com.bhavesh.taskflow.models.Project;
import com.bhavesh.taskflow.models.Task;
import com.bhavesh.taskflow.models.User;
import com.bhavesh.taskflow.repository.TaskRepository;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    public boolean createTask(TaskRequestDTO taskRequest, Project project, User assignedTo) {
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        task.setStatus(TaskStatus.TODO);
        task.setCreatedBy(UserService.getCurrentAuthenticatedUser());
        task.setProject(project);
        task.setAssignedTo(assignedTo);
        return taskRepository.save(task) != null;
    }

    public List<TaskResponseDTO> getTasksForProject(Long projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        List<TaskResponseDTO> taskResponseDTOs = new ArrayList<>();
        for (Task task : tasks) {
            TaskResponseDTO dto = new TaskResponseDTO();
            dto.setTaskTitle(task.getTitle());
            dto.setTaskDescription(task.getDescription());
            dto.setTaskStatus(task.getStatus());
            dto.setDueDate(task.getDueDate());
            dto.setProjectName(task.getProject().getName());
            dto.setAssignedToEmail(task.getAssignedTo() != null ? task.getAssignedTo().getEmail() : null);
            dto.setAssignedToUsername(task.getAssignedTo() != null ? task.getAssignedTo().getName() : null);
            dto.setCreatedBy(task.getCreatedBy().getName());
            taskResponseDTOs.add(dto);
        }
        return taskResponseDTOs;
    }

    
}
