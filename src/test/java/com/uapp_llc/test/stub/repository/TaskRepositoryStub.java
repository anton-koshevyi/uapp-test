package com.uapp_llc.test.stub.repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.uapp_llc.model.Task;
import com.uapp_llc.repository.TaskRepository;
import com.uapp_llc.test.stub.repository.identification.Identification;

public class TaskRepositoryStub
    extends IdentifyingRepositoryStub<Long, Task>
    implements TaskRepository {

  public TaskRepositoryStub(Identification<Task> identification) {
    super(identification);
  }

  @Override
  protected Long getId(Task entity) {
    return entity.getId();
  }

  @Override
  public Task save(Task entity) {
    return super.save(entity);
  }

  @Override
  public Optional<Task> findByIdAndColumnId(Long id, Long columnId) {
    Task entity = super.find(task ->
        Objects.equals(id, task.getId())
            && Objects.equals(columnId, task.getColumn().getId()));
    return Optional.ofNullable(entity);
  }

  @Override
  public Page<Task> findAllByColumnId(Long columnId, Pageable pageable) {
    List<Task> entities = super.findAll().stream()
        .filter(task -> Objects.equals(columnId, task.getColumn().getId()))
        .collect(Collectors.toList());
    return new PageImpl<>(entities);
  }

  @Override
  public void delete(Task entity) {
    super.delete(entity);
  }

}
