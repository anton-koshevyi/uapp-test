package com.uapp_llc.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.uapp_llc.model.Project;
import com.uapp_llc.repository.jpa.ProjectJpaRepository;

@Repository
public class ProjectRepositoryImpl implements ProjectRepository {

  private final ProjectJpaRepository delegate;

  @Autowired
  public ProjectRepositoryImpl(ProjectJpaRepository delegate) {
    this.delegate = delegate;
  }

  @Override
  public Project save(Project entity) {
    return delegate.save(entity);
  }

  @Override
  public Optional<Project> findById(Long id) {
    return delegate.findById(id);
  }

  @Override
  public void delete(Project entity) {
    delegate.delete(entity);
  }

}
