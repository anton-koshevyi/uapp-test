package com.uapp_llc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uapp_llc.model.Column;

public interface ColumnRepository {

  Column save(Column entity);

  Optional<Column> findById(Long id);

  List<Column> findAll();

  Page<Column> findAll(Pageable pageable);

  int count();

  void delete(Column entity);

}
