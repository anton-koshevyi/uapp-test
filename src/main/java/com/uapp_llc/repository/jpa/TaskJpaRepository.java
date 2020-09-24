package com.uapp_llc.repository.jpa;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uapp_llc.model.Task;

public interface TaskJpaRepository extends JpaRepository<Task, Long> {

  Optional<Task> findByIdAndColumnId(Long id, Long columnId);

  Page<Task> findAllByColumnId(Long columnId, Pageable pageable);

}
