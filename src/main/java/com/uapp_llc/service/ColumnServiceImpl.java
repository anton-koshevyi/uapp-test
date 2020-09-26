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
import com.uapp_llc.model.Project;
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
  public Column create(Project project, String name) {
    Column entity = new Column();
    entity.setName(name);
    entity.setIndex(project.getColumns().size());
    entity.setProject(project);
    return repository.save(entity);
  }

  @Transactional
  @Override
  public Column update(Long id, Long projectId, String name) {
    Column entity = this.find(id, projectId);

    if (name != null) {
      entity.setName(name);
    }

    return repository.save(entity);
  }

  @Transactional
  @Override
  public Column changeIndex(Long id, Long projectId, Integer index) {
    Column entity = this.find(id, projectId);
    Integer actual = entity.getIndex();

    if (actual.equals(index)) {
      return entity;
    }

    List<Column> columns = entity.getProject().getColumns();

    if (index < 0 || index >= columns.size()) {
      throw new IllegalActionException(
          "illegalAction.column.changeIndexOutOfBounds", index);
    }

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
  public Column find(Long id, Long projectId) {
    return repository.findByIdAndProjectId(id, projectId)
        .orElseThrow(() -> new NotFoundException(
            "notFound.column.byIdAndProjectId", id, projectId));
  }

  @Override
  public Page<Column> findAll(Long projectId, Pageable pageable) {
    return repository.findAllByProjectId(projectId, pageable);
  }

  @Transactional
  @Override
  public void delete(Long id, Long projectId) {
    Column entity = this.find(id, projectId);
    repository.delete(entity);
  }

}
