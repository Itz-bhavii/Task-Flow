package com.bhavesh.taskflow.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhavesh.taskflow.dtos.TaskRequestDTO;
import com.bhavesh.taskflow.dtos.TaskResponseDTO;
import com.bhavesh.taskflow.services.TaskService;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable("id") Long taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTask(
        @PathVariable("id") Long taskId,
        @RequestBody TaskRequestDTO taskRequest ) {
        boolean updated =taskService.updateTask(taskId, taskRequest);
        if (updated) {
            return ResponseEntity.ok(
                    Map.of("message","Task updated successfully"));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message","Task update failed"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask( @PathVariable("id") Long taskId ) {
        boolean deleted = taskService.deleteTask(taskId);

        if (deleted) {
            return ResponseEntity
                    .noContent()
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message","Task deletion failed"));
    }
}
