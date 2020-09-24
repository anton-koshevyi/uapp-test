package com.uapp_llc.repository;

import java.util.Optional;

import com.uapp_llc.model.Project;

public interface ProjectRepository {

  Project save(Project entity);

  Optional<Project> findById(Long id);

  void delete(Project entity);

}
