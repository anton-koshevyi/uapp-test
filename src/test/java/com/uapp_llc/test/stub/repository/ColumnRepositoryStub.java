package com.uapp_llc.test.stub.repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.uapp_llc.model.Column;
import com.uapp_llc.repository.ColumnRepository;
import com.uapp_llc.test.stub.repository.identification.Identification;

public class ColumnRepositoryStub
    extends IdentifyingRepositoryStub<Long, Column>
    implements ColumnRepository {

  public ColumnRepositoryStub(Identification<Column> identification) {
    super(identification);
  }

  @Override
  protected Long getId(Column entity) {
    return entity.getId();
  }

  @Override
  public Column save(Column entity) {
    return super.save(entity);
  }

  @Override
  public Optional<Column> findByIdAndProjectId(Long id, Long projectId) {
    Column entity = super.find(column ->
        Objects.equals(id, column.getId())
            && Objects.equals(projectId, column.getProject().getId()));
    return Optional.ofNullable(entity);
  }

  @Override
  public Page<Column> findAllByProjectId(Long projectId, Pageable pageable) {
    List<Column> entities = super.findAll().stream()
        .filter(column -> Objects.equals(projectId, column.getProject().getId()))
        .collect(Collectors.toList());
    return new PageImpl<>(entities);
  }

  @Override
  public void delete(Column entity) {
    super.delete(entity);
  }

}
