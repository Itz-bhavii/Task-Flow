package com.bhavesh.taskflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bhavesh.taskflow.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
}
