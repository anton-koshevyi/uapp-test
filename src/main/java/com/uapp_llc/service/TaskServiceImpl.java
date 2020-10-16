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
import com.uapp_llc.model.Task;
import com.uapp_llc.repository.TaskRepository;

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
    entity.setIndex(column.getTasks().size());
    return repository.save(entity);
  }

  @Transactional
  @Override
  public Task update(Long id, Long columnId, String name, String description) {
    Task entity = this.find(id, columnId);

    if (name != null) {
      entity.setName(name);
    }

    if (description != null) {
      entity.setDescription(description);
    }

    return repository.save(entity);
  }

  @Transactional
  @Override
  public Task move(Long id, Long columnId, Column newColumn, Integer newIndex) {
    Task entity = this.find(id, columnId);

    if (newColumn != null) {
      entity.setIndex(newColumn.getTasks().size());
      entity.setColumn(newColumn);
    }

    if (newIndex != null) {
      return changeIndex(entity, newIndex);
    } else {
      return repository.save(entity);
    }
  }

  @Override
  public Task find(Long id, Long columnId) {
    return repository.findByIdAndColumnId(id, columnId)
        .orElseThrow(() -> new NotFoundException(String.format(
            "No task for id '%s' and column id '%s'", id, columnId)));
  }

  @Override
  public Page<Task> findAll(Long columnId, Pageable pageable) {
    return repository.findAllByColumnId(columnId, pageable);
  }

  @Transactional
  @Override
  public void delete(Long id, Long columnId) {
    Task entity = this.find(id, columnId);
    Task last = changeIndex(entity, Math.max(0, entity.getColumn().getTasks().size() - 1));
    repository.delete(last);
  }

  private Task changeIndex(Task entity, Integer index) {
    Integer actual = entity.getIndex();

    if (actual.equals(index)) {
      return entity;
    }

    List<Task> tasks = entity.getColumn().getTasks();

    if (index < 0 || index >= tasks.size()) {
      throw new IllegalActionException("New task index out of bounds: " + index);
    }

    if (actual > index) {
      for (int i = index; i < actual; i++) {
        Task task = tasks.get(i);
        task.setIndex(i + 1);
        repository.save(task);
      }
    } else {
      for (int i = index; i > actual; i--) {
        Task task = tasks.get(i);
        task.setIndex(i - 1);
        repository.save(task);
      }
    }

    entity.setIndex(index);
    return repository.save(entity);
  }

}
