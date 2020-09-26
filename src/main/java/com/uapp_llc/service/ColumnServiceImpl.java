package com.uapp_llc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uapp_llc.exception.IllegalActionException;
import com.uapp_llc.exception.NotFoundException;
import com.uapp_llc.model.Column;
import com.uapp_llc.repository.ColumnRepository;

@Service
public class ColumnServiceImpl implements ColumnService {

  private final ColumnRepository repository;

  @Autowired
  public ColumnServiceImpl(ColumnRepository repository) {
    this.repository = repository;
  }

  @Transactional
  @Override
  public Column create(String name) {
    Column entity = new Column();
    entity.setName(name);
    entity.setIndex(repository.count());
    return repository.save(entity);
  }

  @Transactional
  @Override
  public Column update(Long id, String name) {
    Column entity = this.find(id);

    if (name != null) {
      entity.setName(name);
    }

    return repository.save(entity);
  }

  @Transactional
  @Override
  public Column changeIndex(Long id, Integer index) {
    Column entity = this.find(id);
    Integer actual = entity.getIndex();

    if (actual.equals(index)) {
      return entity;
    }

    if (index < 0 || index >= repository.count()) {
      throw new IllegalActionException("New column index out of bounds: " + index);
    }

    List<Column> columns = repository.findAllByOrderByIndex();

    if (actual > index) {
      int temp = index;

      for (int i = index; i < actual; i++) {
        columns.get(i).setIndex(++temp);
      }
    } else {
      int temp = actual;

      for (int i = actual + 1; i <= index; i++) {
        columns.get(i).setIndex(temp++);
      }
    }

    entity.setIndex(index);
    return repository.save(entity);
  }

  @Override
  public Column find(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NotFoundException(String.format(
            "No column for id '%s'", id)));
  }

  @Override
  public Page<Column> findAll(Pageable pageable) {
    return repository.findAll(pageable);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    Column entity = this.find(id);
    repository.delete(entity);
  }

}
