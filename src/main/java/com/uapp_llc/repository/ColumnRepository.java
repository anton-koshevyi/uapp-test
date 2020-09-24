package com.uapp_llc.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uapp_llc.model.Column;

public interface ColumnRepository {

  Column save(Column entity);

  Optional<Column> findByIdAndProjectId(Long id, Long projectId);

  Page<Column> findAllByProjectId(Long projectId, Pageable pageable);

  void delete(Column entity);

}
