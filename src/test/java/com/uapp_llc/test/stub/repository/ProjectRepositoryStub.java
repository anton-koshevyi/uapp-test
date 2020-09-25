package com.uapp_llc.test.stub.repository;

import java.util.Optional;

import com.uapp_llc.model.Project;
import com.uapp_llc.repository.ProjectRepository;
import com.uapp_llc.test.stub.repository.identification.Identification;

public class ProjectRepositoryStub
    extends IdentifyingRepositoryStub<Long, Project>
    implements ProjectRepository {

  public ProjectRepositoryStub(Identification<Project> identification) {
    super(identification);
  }

  @Override
  protected Long getId(Project entity) {
    return entity.getId();
  }

  @Override
  public Project save(Project entity) {
    return super.save(entity);
  }

  @Override
  public Optional<Project> findById(Long id) {
    Project entity = super.find(id);
    return Optional.ofNullable(entity);
  }

  @Override
  public void delete(Project entity) {
    super.delete(entity);
  }

}
