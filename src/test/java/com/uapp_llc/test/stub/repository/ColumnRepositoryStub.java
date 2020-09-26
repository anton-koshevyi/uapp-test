package com.uapp_llc.test.stub.repository;

import java.util.List;
import java.util.Optional;

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
  public Optional<Column> findById(Long id) {
    Column entity = super.find(id);
    return Optional.ofNullable(entity);
  }

  @Override
  public List<Column> findAll() {
    return super.findAll();
  }

  @Override
  public Page<Column> findAll(Pageable pageable) {
    List<Column> entities = super.findAll();
    return new PageImpl<>(entities);
  }

  @Override
  public int count() {
    return super.size();
  }

  @Override
  public void delete(Column entity) {
    super.delete(entity);
  }

}
