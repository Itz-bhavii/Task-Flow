package com.bhavesh.taskflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bhavesh.taskflow.models.ProjectMember;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    ProjectMember findByProjectIdAndUserId(Long id, Long id2);

    List<ProjectMember> findByUserId(Long id);
    void deleteByProjectId(Long projectId);

    boolean existsByProjectId(Long projectId);

    List<ProjectMember> findByProjectId(Long projectId);
}
