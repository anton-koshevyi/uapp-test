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
import com.uapp_llc.test.model.ModelFactoryProducer;
import com.uapp_llc.test.model.column.ColumnType;
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
    identification.setStrategy(e -> e.setId(1L));

    service.create(
        "Monday tasks"
    );

    Assertions
        .assertThat(repository.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Column.class))
        .isEqualTo(new Column()
            .setId(1L)
            .setName("Monday tasks")
            .setIndex(0));
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
    identification.setStrategy(e -> e.setId(1L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY));

    service.update(
        1L,
        "Tuesday tasks"
    );

    Assertions
        .assertThat(repository.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Column.class))
        .isEqualTo(ModelFactoryProducer.getFactory(Column.class)
            .createModel(ColumnType.MONDAY)
            .setId(1L)
            .setName("Tuesday tasks"));
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
    identification.setStrategy(e -> e.setId(1L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY));
    identification.setStrategy(e -> e.setId(2L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.WEDNESDAY));

    Assertions
        .assertThatThrownBy(() -> service.changeIndex(1L, newIndex))
        .isExactlyInstanceOf(IllegalActionException.class)
        .hasMessage("New column index out of bounds: " + newIndex);
  }

  @Test
  public void changeIndex_whenIndexEqualToActual_expectNoChanges() {
    identification.setStrategy(e -> e.setId(1L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY)
        .setIndex(0));
    identification.setStrategy(e -> e.setId(2L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.WEDNESDAY)
        .setIndex(1));

    service.changeIndex(1L, 0);

    Assertions
        .assertThat(repository.findAllByOrderByIndex())
        .usingComparatorForType(ComparatorFactory.getComparator(Column.class), Column.class)
        .containsExactlyInAnyOrder(
            ModelFactoryProducer.getFactory(Column.class)
                .createModel(ColumnType.MONDAY)
                .setId(1L)
                .setIndex(0),
            ModelFactoryProducer.getFactory(Column.class)
                .createModel(ColumnType.WEDNESDAY)
                .setId(2L)
                .setIndex(1)
        );
  }

  @Test
  public void changeIndex_whenIndexAfterActual() {
    identification.setStrategy(e -> e.setId(1L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY)
        .setIndex(0));
    identification.setStrategy(e -> e.setId(2L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.WEDNESDAY)
        .setIndex(1));

    service.changeIndex(2L, 0);

    Assertions
        .assertThat(repository.findAllByOrderByIndex())
        .usingComparatorForType(ComparatorFactory.getComparator(Column.class), Column.class)
        .containsExactlyInAnyOrder(
            ModelFactoryProducer.getFactory(Column.class)
                .createModel(ColumnType.MONDAY)
                .setId(1L)
                .setIndex(1),
            ModelFactoryProducer.getFactory(Column.class)
                .createModel(ColumnType.WEDNESDAY)
                .setId(2L)
                .setIndex(0)
        );
  }

  @Test
  public void changeIndex_whenIndexBeforeActual() {
    identification.setStrategy(e -> e.setId(1L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY)
        .setIndex(0));
    identification.setStrategy(e -> e.setId(2L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.WEDNESDAY)
        .setIndex(1));

    service.changeIndex(1L, 1);

    Assertions
        .assertThat(repository.findAllByOrderByIndex())
        .usingComparatorForType(ComparatorFactory.getComparator(Column.class), Column.class)
        .containsExactlyInAnyOrder(
            ModelFactoryProducer.getFactory(Column.class)
                .createModel(ColumnType.MONDAY)
                .setId(1L)
                .setIndex(1),
            ModelFactoryProducer.getFactory(Column.class)
                .createModel(ColumnType.WEDNESDAY)
                .setId(2L)
                .setIndex(0)
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
    identification.setStrategy(e -> e.setId(1L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY));

    Assertions
        .assertThat(service.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Column.class))
        .isEqualTo(ModelFactoryProducer.getFactory(Column.class)
            .createModel(ColumnType.MONDAY)
            .setId(1L));
  }

  @Test
  public void findAll() {
    identification.setStrategy(e -> e.setId(1L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY));
    identification.setStrategy(e -> e.setId(2L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.WEDNESDAY));

    Assertions
        .assertThat(service.findAll(Pageable.unpaged()))
        .usingComparatorForType(ComparatorFactory.getComparator(Column.class), Column.class)
        .containsExactlyInAnyOrder(
            ModelFactoryProducer.getFactory(Column.class)
                .createModel(ColumnType.MONDAY)
                .setId(1L),
            ModelFactoryProducer.getFactory(Column.class)
                .createModel(ColumnType.WEDNESDAY)
                .setId(2L)
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
  public void delete() {
    identification.setStrategy(e -> e.setId(1L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY));

    service.delete(1L);

    Assertions
        .assertThat(repository.find(1L))
        .isNull();
  }

}
