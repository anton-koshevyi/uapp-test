package com.uapp_llc.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.uapp_llc.model.Task;
import com.uapp_llc.repository.jpa.TaskJpaRepository;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

  private final TaskJpaRepository delegate;

  @Autowired
  public TaskRepositoryImpl(TaskJpaRepository delegate) {
    this.delegate = delegate;
  }

  @Override
  public Task save(Task entity) {
    return delegate.save(entity);
  }

  @Override
  public Optional<Task> findByIdAndColumnId(Long id, Long columnId) {
    return delegate.findByIdAndColumnId(id, columnId);
  }

  @Override
  public Page<Task> findAllByColumnId(Long columnId, Pageable pageable) {
    return delegate.findAllByColumnId(columnId, pageable);
  }

  @Override
  public void delete(Task entity) {
    delegate.delete(entity);
  }

}
