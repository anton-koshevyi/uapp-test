package com.uapp_llc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Project;

public interface ColumnService {

  Column create(Project project, String name);

  Column update(Long id, Long projectId, String name);

  Column changePosition(Long id, Long projectId, Integer change);

  Column find(Long id, Long projectId);

  Page<Column> findAll(Long projectId, Pageable pageable);

  void delete(Long id, Long projectId);

}
