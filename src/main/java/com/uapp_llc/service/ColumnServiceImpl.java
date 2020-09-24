package com.uapp_llc.service;

import java.util.ArrayList;
import java.util.Collections;
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
import com.uapp_llc.util.NullableUtil;

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
    entity.setProject(project);
    return repository.save(entity);
  }

  @Transactional
  @Override
  public Column update(Long id, Long projectId, String name) {
    Column entity = this.find(id, projectId);
    NullableUtil.set(entity::setName, name);
    return repository.save(entity);
  }

  @Override
  public Column changePosition(Long id, Long projectId, Integer change) {
    Column entity = this.find(id, projectId);
    Project project = entity.getProject();
    List<Column> columns = new ArrayList<>(project.getColumns());

    for (int i = 0; i < columns.size(); i++) {
      Column task = columns.get(i);

      if (id.equals(task.getId())) {
        try {
          Collections.swap(columns, i, change);
          break;
        } catch (IndexOutOfBoundsException e) {
          throw new IllegalActionException(
              "illegalAction.column.positionOutOfBounds", change);
        }
      }
    }

    project.setColumns(columns);
    entity.setProject(project);
    return repository.save(entity);
  }

  @Override
  public Column find(Long id, Long projectId) {
    return repository.findByIdAndProjectId(id, projectId)
        .orElseThrow(() -> new NotFoundException(
            "notFound.column.byIdAndProject", id, projectId));
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
