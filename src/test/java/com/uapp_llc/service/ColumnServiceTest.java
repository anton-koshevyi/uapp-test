package com.uapp_llc.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.Pageable;

import com.uapp_llc.exception.IllegalActionException;
import com.uapp_llc.exception.NotFoundException;
import com.uapp_llc.model.Column;
import com.uapp_llc.test.comparator.ComparatorFactory;
import com.uapp_llc.test.model.factory.ModelFactory;
import com.uapp_llc.test.model.mutator.ColumnMutators;
import com.uapp_llc.test.model.type.ColumnType;
import com.uapp_llc.test.stub.repository.ColumnRepositoryStub;
import com.uapp_llc.test.stub.repository.identification.IdentificationContext;

public class ColumnServiceTest {

  private IdentificationContext<Column> identification;
  private ColumnRepositoryStub repository;
  private ColumnService service;

  @BeforeEach
  public void setUp() {
    identification = new IdentificationContext<>();
    repository = new ColumnRepositoryStub(identification);
    service = new ColumnServiceImpl(repository);
  }

  @Test
  public void create() {
    identification.setStrategy(ColumnMutators.id(1L)::accept);

    service.create("Monday tasks");

    Assertions
        .assertThat(repository.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Column.class))
        .isEqualTo(ModelFactory
            .createWrapper(ColumnType.MONDAY)
            .with(ColumnMutators.id(1L))
            .getModel());
  }

  @Test
  public void update_whenNoEntityWithIdAndProjectId_expectException() {
    Assertions
        .assertThatThrownBy(() -> service.update(1L, "Tuesday tasks"))
        .isExactlyInstanceOf(NotFoundException.class)
        .hasMessage("No column for id '1'");
  }

  @Test
  public void update() {
    identification.setStrategy(ColumnMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createModel(ColumnType.MONDAY));

    service.update(1L, "Tuesday tasks");

    Assertions
        .assertThat(repository.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Column.class))
        .isEqualTo(ModelFactory
            .createWrapper(ColumnType.MONDAY)
            .with(ColumnMutators.id(1L))
            .with(ColumnMutators.name("Tuesday tasks"))
            .getModel());
  }

  @Test
  public void changeIndex_whenNoEntityWithIdAndProjectId_expectException() {
    Assertions
        .assertThatThrownBy(() -> service.changeIndex(1L, 0))
        .isExactlyInstanceOf(NotFoundException.class)
        .hasMessage("No column for id '1'");
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 2})
  public void changeIndex_whenIndexOutOfBounds_expectException(int newIndex) {
    identification.setStrategy(ColumnMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createModel(ColumnType.MONDAY));
    identification.setStrategy(ColumnMutators.id(2L)::accept);
    repository.save(ModelFactory
        .createModel(ColumnType.WEDNESDAY));

    Assertions
        .assertThatThrownBy(() -> service.changeIndex(1L, newIndex))
        .isExactlyInstanceOf(IllegalActionException.class)
        .hasMessage("New column index out of bounds: " + newIndex);
  }

  @Test
  public void changeIndex_whenIndexEqualToActual_expectNoChanges() {
    identification.setStrategy(ColumnMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.index(0))
        .getModel());
    identification.setStrategy(ColumnMutators.id(2L)::accept);
    repository.save(ModelFactory
        .createWrapper(ColumnType.WEDNESDAY)
        .with(ColumnMutators.index(1))
        .getModel());

    service.changeIndex(1L, 0);

    Assertions
        .assertThat(repository.findAllByOrderByIndex())
        .usingComparatorForType(ComparatorFactory.getComparator(Column.class), Column.class)
        .containsExactlyInAnyOrder(
            ModelFactory
                .createWrapper(ColumnType.MONDAY)
                .with(ColumnMutators.id(1L))
                .with(ColumnMutators.index(0))
                .getModel(),
            ModelFactory
                .createWrapper(ColumnType.WEDNESDAY)
                .with(ColumnMutators.id(2L))
                .with(ColumnMutators.index(1))
                .getModel()
        );
  }

  @Test
  public void changeIndex_whenIndexAfterActual() {
    identification.setStrategy(ColumnMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.index(0))
        .getModel());
    identification.setStrategy(ColumnMutators.id(2L)::accept);
    repository.save(ModelFactory
        .createWrapper(ColumnType.WEDNESDAY)
        .with(ColumnMutators.index(1))
        .getModel());

    service.changeIndex(2L, 0);

    Assertions
        .assertThat(repository.findAllByOrderByIndex())
        .usingComparatorForType(ComparatorFactory.getComparator(Column.class), Column.class)
        .containsExactlyInAnyOrder(
            ModelFactory
                .createWrapper(ColumnType.MONDAY)
                .with(ColumnMutators.id(1L))
                .with(ColumnMutators.index(1))
                .getModel(),
            ModelFactory
                .createWrapper(ColumnType.WEDNESDAY)
                .with(ColumnMutators.id(2L))
                .with(ColumnMutators.index(0))
                .getModel()
        );
  }

  @Test
  public void changeIndex_whenIndexBeforeActual() {
    identification.setStrategy(ColumnMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.index(0))
        .getModel());
    identification.setStrategy(ColumnMutators.id(2L)::accept);
    repository.save(ModelFactory
        .createWrapper(ColumnType.WEDNESDAY)
        .with(ColumnMutators.index(1))
        .getModel());

    service.changeIndex(1L, 1);

    Assertions
        .assertThat(repository.findAllByOrderByIndex())
        .usingComparatorForType(ComparatorFactory.getComparator(Column.class), Column.class)
        .containsExactlyInAnyOrder(
            ModelFactory
                .createWrapper(ColumnType.MONDAY)
                .with(ColumnMutators.id(1L))
                .with(ColumnMutators.index(1))
                .getModel(),
            ModelFactory
                .createWrapper(ColumnType.WEDNESDAY)
                .with(ColumnMutators.id(2L))
                .with(ColumnMutators.index(0))
                .getModel()
        );
  }

  @Test
  public void find_whenNoEntityWithIdAndProjectId_expectException() {
    Assertions
        .assertThatThrownBy(() -> service.find(1L))
        .isExactlyInstanceOf(NotFoundException.class)
        .hasMessage("No column for id '1'");
  }

  @Test
  public void find() {
    identification.setStrategy(ColumnMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createModel(ColumnType.MONDAY));

    Assertions
        .assertThat(service.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Column.class))
        .isEqualTo(ModelFactory
            .createWrapper(ColumnType.MONDAY)
            .with(ColumnMutators.id(1L))
            .getModel());
  }

  @Test
  public void findAll() {
    identification.setStrategy(ColumnMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createModel(ColumnType.MONDAY));
    identification.setStrategy(ColumnMutators.id(2L)::accept);
    repository.save(ModelFactory
        .createModel(ColumnType.WEDNESDAY));

    Assertions
        .assertThat(service.findAll(Pageable.unpaged()))
        .usingComparatorForType(ComparatorFactory.getComparator(Column.class), Column.class)
        .containsExactlyInAnyOrder(
            ModelFactory
                .createWrapper(ColumnType.MONDAY)
                .with(ColumnMutators.id(1L))
                .getModel(),
            ModelFactory
                .createWrapper(ColumnType.WEDNESDAY)
                .with(ColumnMutators.id(2L))
                .getModel()
        );
  }

  @Test
  public void delete_whenNoEntityWithIdAndProjectId_expectException() {
    Assertions
        .assertThatThrownBy(() -> service.delete(1L))
        .isExactlyInstanceOf(NotFoundException.class)
        .hasMessage("No column for id '1'");
  }

  @Test
  public void delete_whenLastEntity_expectChangeIndexTo0() {
    identification.setStrategy(ColumnMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .getModel());

    service.delete(1L);

    Assertions
        .assertThat(repository.find(1L))
        .isNull();
  }

  @Test
  public void delete_expectChangeIndexToLast() {
    identification.setStrategy(ColumnMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.index(0))
        .getModel());
    identification.setStrategy(ColumnMutators.id(2L)::accept);
    repository.save(ModelFactory
        .createWrapper(ColumnType.WEDNESDAY)
        .with(ColumnMutators.index(1))
        .getModel());

    service.delete(1L);

    Assertions
        .assertThat(repository.find(1L))
        .isNull();
    Assertions
        .assertThat(repository.find(2L))
        .usingComparator(ComparatorFactory.getComparator(Column.class))
        .isEqualTo(ModelFactory
            .createWrapper(ColumnType.WEDNESDAY)
            .with(ColumnMutators.id(2L))
            .with(ColumnMutators.index(0))
            .getModel());
  }

}
