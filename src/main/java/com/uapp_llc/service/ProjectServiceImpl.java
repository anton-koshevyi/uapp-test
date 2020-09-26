package com.uapp_llc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uapp_llc.exception.NotFoundException;
import com.uapp_llc.model.Project;
import com.uapp_llc.repository.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository repository;

  @Autowired
  public ProjectServiceImpl(ProjectRepository repository) {
    this.repository = repository;
  }

  @Transactional
  @Override
  public Project create() {
    Project entity = new Project();
    return repository.save(entity);
  }

  @Override
  public Project find(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NotFoundException(String.format(
            "No project for id '%s'", id)));
  }

  @Transactional
  @Override
  public void delete(Long id) {
    Project entity = this.find(id);
    repository.delete(entity);
  }

}
