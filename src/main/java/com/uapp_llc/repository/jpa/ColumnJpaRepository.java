package com.uapp_llc.repository.jpa;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uapp_llc.model.Column;

public interface ColumnJpaRepository extends JpaRepository<Column, Long> {

  Optional<Column> findByIdAndProjectId(Long id, Long projectId);

  Page<Column> findAllByProjectId(Long projectId, Pageable pageable);

}
