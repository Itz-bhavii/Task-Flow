package com.bhavesh.taskflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bhavesh.taskflow.models.ProjectMember;

@Repository
public interface ProductMemberRepository extends JpaRepository<ProjectMember, Long> {
    
}
