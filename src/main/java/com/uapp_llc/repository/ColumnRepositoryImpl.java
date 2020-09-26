package com.uapp_llc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
  public Optional<Column> findById(Long id) {
    return delegate.findById(id);
  }

  @Override
  public Page<Column> findAll(Pageable pageable) {
    return delegate.findAll(pageable);
  }

  @Override
  public List<Column> findAllByOrderByIndex() {
    return delegate.findAll(Sort.by("index"));
  }

  @Override
  public int count() {
    return (int) delegate.count();
  }

  @Override
  public void delete(Column entity) {
    delegate.delete(entity);
  }

}
