package com.bhavesh.taskflow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhavesh.taskflow.dtos.TaskRequestDTO;
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

    
}
