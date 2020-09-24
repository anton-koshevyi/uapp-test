package com.uapp_llc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Task;

public interface TaskService {

  Task create(Column column, String name, String description);

  Task update(Long id, Long columnId, String name, String description);

  Task changePosition(Long id, Long columnId, Integer change);

  Task changeColumn(Long id, Long actualId, Column change);

  Task find(Long id, Long columnId);

  Page<Task> findAll(Long columnId, Pageable pageable);

  void delete(Long id, Long columnId);

}
