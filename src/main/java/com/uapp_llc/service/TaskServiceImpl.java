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
import com.uapp_llc.model.Task;
import com.uapp_llc.repository.TaskRepository;
import com.uapp_llc.util.NullableUtil;

@Service
public class TaskServiceImpl implements TaskService {

  private final TaskRepository repository;

  @Autowired
  public TaskServiceImpl(TaskRepository repository) {
    this.repository = repository;
  }

  @Transactional
  @Override
  public Task create(Column column, String name, String description) {
    Task entity = new Task();
    entity.setName(name);
    entity.setDescription(description);
    entity.setColumn(column);
    return repository.save(entity);
  }

  @Transactional
  @Override
  public Task update(Long id, Long columnId, String name, String description) {
    Task entity = this.find(id, columnId);
    NullableUtil.set(entity::setName, name);
    NullableUtil.set(entity::setDescription, description);
    return repository.save(entity);
  }

  @Override
  public Task changePosition(Long id, Long columnId, Integer change) {
    Task entity = this.find(id, columnId);
    Column column = entity.getColumn();
    List<Task> tasks = new ArrayList<>(column.getTasks());

    for (int i = 0; i < tasks.size(); i++) {
      Task task = tasks.get(i);

      if (id.equals(task.getId())) {
        try {
          Collections.swap(tasks, i, change);
          break;
        } catch (IndexOutOfBoundsException e) {
          throw new IllegalActionException(
              "illegalAction.task.positionOutOfBounds", change);
        }
      }
    }

    column.setTasks(tasks);
    entity.setColumn(column);
    return repository.save(entity);
  }

  @Transactional
  @Override
  public Task changeColumn(Long id, Long actualId, Column change) {
    Task entity = this.find(id, actualId);
    entity.setColumn(change);
    return repository.save(entity);
  }

  @Override
  public Task find(Long id, Long columnId) {
    return repository.findByIdAndColumnId(id, columnId)
        .orElseThrow(() -> new NotFoundException(
            "notFound.task.byIdAndColumn", id, columnId));
  }

  @Override
  public Page<Task> findAll(Long columnId, Pageable pageable) {
    return repository.findAllByColumnId(columnId, pageable);
  }

  @Transactional
  @Override
  public void delete(Long id, Long columnId) {
    Task entity = this.find(id, columnId);
    repository.delete(entity);
  }

}
