package com.uapp_llc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uapp_llc.model.Column;

public interface ColumnService {

  Column create(String name);

  Column update(Long id, String name);

  Column changeIndex(Long id, Integer index);

  Column find(Long id);

  Page<Column> findAll(Pageable pageable);

  void delete(Long id);

}
