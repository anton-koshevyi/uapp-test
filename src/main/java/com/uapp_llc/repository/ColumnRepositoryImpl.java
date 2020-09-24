package com.uapp_llc.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.uapp_llc.model.Column;
import com.uapp_llc.repository.jpa.ColumnJpaRepository;

@Repository
public class ColumnRepositoryImpl implements ColumnRepository {

  private final ColumnJpaRepository delegate;

  @Autowired
  public ColumnRepositoryImpl(ColumnJpaRepository delegate) {
    this.delegate = delegate;
  }

  @Override
  public Column save(Column entity) {
    return delegate.save(entity);
  }

  @Override
  public Optional<Column> findByIdAndProjectId(Long id, Long projectId) {
    return delegate.findByIdAndProjectId(id, projectId);
  }

  @Override
  public Page<Column> findAllByProjectId(Long projectId, Pageable pageable) {
    return delegate.findAllByProjectId(projectId, pageable);
  }

  @Override
  public void delete(Column entity) {
    delegate.delete(entity);
  }

}
