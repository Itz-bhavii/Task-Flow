package com.bhavesh.taskflow.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhavesh.taskflow.dtos.TaskRequestDTO;
import com.bhavesh.taskflow.dtos.TaskResponseDTO;
import com.bhavesh.taskflow.enums.ProjectRole;
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

    @Autowired
    private ProjectMemberService projectMemberService;

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
        if(!projectMemberService.isUserProjectMember(projectId, UserService.getCurrentAuthenticatedUser().getId())) {
            throw new RuntimeException("You are not a member of this project");
        }
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
            throw new RuntimeException("Task not found");
        }   
        return convertTaskToDTO(task);
    }

    public TaskResponseDTO convertTaskToDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
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
            throw new RuntimeException("Task not found");
        }
        if(!canUserUpdateTask(task)) {
                throw new RuntimeException("You are not the owner of this task");
        }

        ProjectRole userRole = projectMemberService.getUserProjectRole(task.getProject().getId(), UserService.getCurrentAuthenticatedUser().getId());

        if(userRole == ProjectRole.MEMBER && taskrequest.getTaskStatus() != null) {
            task.setStatus(taskrequest.getTaskStatus());
            return taskRepository.save(task) != null;
        } 
        
        // Admin can update all fields

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

    private boolean canUserUpdateTask(Task task) {
        User currentUser = UserService.getCurrentAuthenticatedUser();
        return task.getCreatedBy().getId().equals(currentUser.getId()) || 
               (task.getAssignedTo() != null && task.getAssignedTo().getId().equals(currentUser.getId()));
    }

    private boolean isProjectAdmin(Task task) {
        User currentUser = UserService.getCurrentAuthenticatedUser();
        try{
            ProjectRole role = projectMemberService.getUserProjectRole(task.getProject().getId(), currentUser.getId());
            return role == ProjectRole.ADMIN;
        } catch (Exception e) {
            throw new RuntimeException("Task does not exists or you are not a member of the project");
        }
    }

    public boolean deleteTask(Long taskId) {

        if (!taskRepository.existsById(taskId) && !isProjectAdmin(taskRepository.findById(taskId).orElse(null))) {
            throw new RuntimeException("Task not found or you are not the owner of this task");
        }
        try {
            taskRepository.deleteById(taskId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete task");
        }
        return !taskRepository.existsById(taskId);
    }

    public List<Task> getCurrentUserAssignedTasks(){
        User currentUser = UserService.getCurrentAuthenticatedUser();
        return taskRepository.findByAssignedToId(currentUser.getId());  
    }

    public List<Task> getCurrentUserCompletedTasks(){
        User currentUser = UserService.getCurrentAuthenticatedUser();
        return taskRepository.findByAssignedToIdAndStatusIn(currentUser.getId(), List.of(TaskStatus.DONE));
    }

    public List<Task> getCurrentUserPendingTasks(){
        User currentUser = UserService.getCurrentAuthenticatedUser();
        return taskRepository.findByAssignedToIdAndStatusIn(currentUser.getId(), List.of(TaskStatus.IN_PROGRESS,TaskStatus.TODO));
    }

    public List<Task> getCurrentUserOverdueTasks() {
        User currentUser = UserService.getCurrentAuthenticatedUser();
        List<Task> assignedTasks = taskRepository.findByAssignedToId(currentUser.getId());
        List<Task> overdueTasks = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for(Task task : assignedTasks) {
            if(task.getDueDate() != null && task.getDueDate().isBefore(now) && task.getStatus() != TaskStatus.DONE) {
                overdueTasks.add(task);
            }
        }
        return overdueTasks;
    }

    public boolean deleteTasksForProjectId(Long projectId) {
        taskRepository.deleteByProjectId(projectId);
        return taskRepository.existsByProjectId(projectId);
    }
    
}
