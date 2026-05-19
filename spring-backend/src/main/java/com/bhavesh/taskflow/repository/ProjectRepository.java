package com.bhavesh.taskflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bhavesh.taskflow.models.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    public boolean existsByName(String name);
    public List<Project> findByCreatedById(Long userId);

}
