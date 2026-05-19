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

    @Autowired
    private UserService userService;

    public boolean createTask(TaskRequestDTO taskRequest, Project project, User assignedTo) {
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        task.setStatus(taskRequest.getTaskStatus() != null ? taskRequest.getTaskStatus() : TaskStatus.TODO);
        task.setCreatedBy(UserService.getCurrentAuthenticatedUser());
        task.setProject(project);
        task.setAssignedTo(assignedTo);
        return taskRepository.save(task) != null;
    }

    public List<TaskResponseDTO> getTasksForProject(Long projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        List<TaskResponseDTO> taskResponseDTOs = new ArrayList<>();
        for (Task task : tasks) {
            TaskResponseDTO dto = convertTaskToDTO(task);
            taskResponseDTOs.add(dto);
        }
        return taskResponseDTOs;
    }

    public TaskResponseDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return null;
        }   
        return convertTaskToDTO(task);
    }

    public TaskResponseDTO convertTaskToDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setTaskTitle(task.getTitle());
        dto.setTaskDescription(task.getDescription());
        dto.setTaskStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        dto.setProjectName(task.getProject().getName());
        dto.setAssignedToEmail(task.getAssignedTo() != null ? task.getAssignedTo().getEmail() : null);
        dto.setAssignedToUsername(task.getAssignedTo() != null ? task.getAssignedTo().getName() : null);
        dto.setCreatedBy(task.getCreatedBy().getName());
        return dto;
    }

    public boolean updateTask(Long taskId, TaskRequestDTO taskrequest) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return false;
        }
        if (taskrequest.getTitle() != null) {
            task.setTitle(taskrequest.getTitle());
        }
        if (taskrequest.getDescription() != null) {
            task.setDescription(taskrequest.getDescription());
        }
        if (taskrequest.getDueDate() != null) {
            task.setDueDate(taskrequest.getDueDate());
        }
        if (taskrequest.getTaskStatus() != null) {
            task.setStatus(taskrequest.getTaskStatus());
        }
        if(taskrequest.getAssignedToEmail() != null) {
            User assignedTo = userService.getUserByEmail(taskrequest.getAssignedToEmail());
            task.setAssignedTo(assignedTo);
        }
        return taskRepository.save(task) != null;   
    }
    
}
