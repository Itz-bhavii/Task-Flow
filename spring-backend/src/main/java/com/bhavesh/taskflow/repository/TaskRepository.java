package com.bhavesh.taskflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bhavesh.taskflow.enums.TaskStatus;
import com.bhavesh.taskflow.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProjectId(Long projectId);
    List<Task> findByAssignedToId(Long userId);
    List<Task> findByAssignedToIdAndStatusIn(Long userId,List<TaskStatus> status);
    void deleteByProjectId(Long projectId);
    boolean existsByProjectId(Long projectId);
    
}
