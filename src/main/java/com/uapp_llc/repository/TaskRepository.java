package com.uapp_llc.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uapp_llc.model.Task;

public interface TaskRepository {

  Task save(Task entity);

  Optional<Task> findByIdAndColumnId(Long id, Long columnId);

  Page<Task> findAllByColumnId(Long columnId, Pageable pageable);

  void delete(Task entity);

}
