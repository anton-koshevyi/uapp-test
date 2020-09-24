package com.uapp_llc.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uapp_llc.model.Project;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {
}
