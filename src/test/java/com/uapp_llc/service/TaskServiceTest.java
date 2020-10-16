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
import com.uapp_llc.model.Task;
import com.uapp_llc.test.comparator.ComparatorFactory;
import com.uapp_llc.test.model.factory.ModelFactory;
import com.uapp_llc.test.model.mutator.ColumnMutators;
import com.uapp_llc.test.model.mutator.TaskMutators;
import com.uapp_llc.test.model.type.ColumnType;
import com.uapp_llc.test.model.type.TaskType;
import com.uapp_llc.test.stub.repository.TaskRepositoryStub;
import com.uapp_llc.test.stub.repository.identification.IdentificationContext;

public class TaskServiceTest {

  private IdentificationContext<Task> identification;
  private TaskRepositoryStub repository;
  private TaskService service;

  @BeforeEach
  public void setUp() {
    identification = new IdentificationContext<>();
    repository = new TaskRepositoryStub(identification);
    service = new TaskServiceImpl(repository);
  }

  @Test
  public void create() {
    Column column = ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.id(2L))
        .getModel();
    identification.setStrategy(TaskMutators.id(1L)::accept);

    service.create(
        column,
        "Job",
        "Go to job"
    );

    Assertions
        .assertThat(repository.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Task.class))
        .isEqualTo(ModelFactory
            .createWrapper(TaskType.JOB)
            .with(TaskMutators.id(1L))
            .with(TaskMutators.column(ModelFactory
                .createWrapper(ColumnType.MONDAY)
                .with(ColumnMutators.id(2L))
                .getModel()))
            .getModel());
  }

  @Test
  public void update_whenNoEntityWithIdAndColumnId_expectException() {
    Assertions
        .assertThatThrownBy(() -> service.update(1L, 2L, "Meeting", "Meet John"))
        .isExactlyInstanceOf(NotFoundException.class)
        .hasMessage("No task for id '1' and column id '2'");
  }

  @Test
  public void update() {
    Column column = ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.id(2L))
        .getModel();
    identification.setStrategy(TaskMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createWrapper(TaskType.JOB)
        .with(TaskMutators.column(column))
        .getModel());

    service.update(
        1L,
        2L,
        "Meeting",
        "Meet John"
    );

    Assertions
        .assertThat(repository.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Task.class))
        .isEqualTo(ModelFactory
            .createWrapper(TaskType.JOB)
            .with(TaskMutators.id(1L))
            .with(TaskMutators.name("Meeting"))
            .with(TaskMutators.description("Meet John"))
            .with(TaskMutators.column(ModelFactory
                .createWrapper(ColumnType.MONDAY)
                .with(ColumnMutators.id(2L))
                .getModel()))
            .getModel());
  }

  @Test
  public void move_whenNotEntityWithIdAndColumnId_expectException() {
    Assertions
        .assertThatThrownBy(() -> service.move(2L, 1L, null, 0))
        .isExactlyInstanceOf(NotFoundException.class)
        .hasMessage("No task for id '2' and column id '1'");
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 2})
  public void move_whenIndexOutOfBounds_expectException(int newIndex) {
    Column column = ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.id(1L))
        .getModel();
    identification.setStrategy(TaskMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createWrapper(TaskType.JOB)
        .with(TaskMutators.index(0))
        .with(TaskMutators.column(column))
        .getModel());
    identification.setStrategy(TaskMutators.id(2L)::accept);
    repository.save(ModelFactory
        .createWrapper(TaskType.MEETING)
        .with(TaskMutators.index(1))
        .with(TaskMutators.column(column))
        .getModel());

    Assertions
        .assertThatThrownBy(() -> service.move(2L, 1L, null, newIndex))
        .isExactlyInstanceOf(IllegalActionException.class)
        .hasMessage("New task index out of bounds: " + newIndex);
  }

  @Test
  public void move_whenNewIndexNotNull_andNewIndexEqualToActual_expectNoChanges() {
    Column column = ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.id(1L))
        .getModel();
    identification.setStrategy(TaskMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createWrapper(TaskType.JOB)
        .with(TaskMutators.index(0))
        .with(TaskMutators.column(column))
        .getModel());
    identification.setStrategy(TaskMutators.id(2L)::accept);
    repository.save(ModelFactory
        .createWrapper(TaskType.MEETING)
        .with(TaskMutators.index(1))
        .with(TaskMutators.column(column))
        .getModel());

    service.move(2L, 1L, null, 1);

    Assertions
        .assertThat(repository.findAll())
        .usingComparatorForType(ComparatorFactory.getComparator(Task.class), Task.class)
        .containsExactlyInAnyOrder(
            ModelFactory
                .createWrapper(TaskType.JOB)
                .with(TaskMutators.id(1L))
                .with(TaskMutators.index(0))
                .with(TaskMutators.column(ModelFactory
                    .createWrapper(ColumnType.MONDAY)
                    .with(ColumnMutators.id(1L))
                    .getModel()))
                .getModel(),
            ModelFactory
                .createWrapper(TaskType.MEETING)
                .with(TaskMutators.id(2L))
                .with(TaskMutators.index(1))
                .with(TaskMutators.column(ModelFactory
                    .createWrapper(ColumnType.MONDAY)
                    .with(ColumnMutators.id(1L))
                    .getModel()))
                .getModel()
        );
  }

  @Test
  public void move_whenNewIndexNotNull_andNewIndexAfterActual() {
    Column column = ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.id(1L))
        .getModel();
    identification.setStrategy(TaskMutators.id(1L)::accept);
    Task job = repository.save(ModelFactory
        .createWrapper(TaskType.JOB)
        .with(TaskMutators.index(0))
        .with(TaskMutators.column(column))
        .getModel());
    identification.setStrategy(TaskMutators.id(2L)::accept);
    Task meeting = repository.save(ModelFactory
        .createWrapper(TaskType.MEETING)
        .with(TaskMutators.index(1))
        .with(TaskMutators.column(column))
        .getModel());
    ColumnMutators.tasks(job, meeting).accept(column);

    service.move(1L, 1L, null, 1);

    Assertions
        .assertThat(repository.findAll())
        .usingComparatorForType(ComparatorFactory.getComparator(Task.class), Task.class)
        .containsExactlyInAnyOrder(
            ModelFactory
                .createWrapper(TaskType.JOB)
                .with(TaskMutators.id(1L))
                .with(TaskMutators.index(1))
                .with(TaskMutators.column(ModelFactory
                    .createWrapper(ColumnType.MONDAY)
                    .with(ColumnMutators.id(1L))
                    .getModel()))
                .getModel(),
            ModelFactory
                .createWrapper(TaskType.MEETING)
                .with(TaskMutators.id(2L))
                .with(TaskMutators.index(0))
                .with(TaskMutators.column(ModelFactory
                    .createWrapper(ColumnType.MONDAY)
                    .with(ColumnMutators.id(1L))
                    .getModel()))
                .getModel()
        );
  }

  @Test
  public void move_whenNewIndexNotNull_andNewIndexBeforeActual() {
    Column column = ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.id(1L))
        .getModel();
    identification.setStrategy(TaskMutators.id(1L)::accept);
    Task job = repository.save(ModelFactory
        .createWrapper(TaskType.JOB)
        .with(TaskMutators.index(0))
        .with(TaskMutators.column(column))
        .getModel());
    identification.setStrategy(TaskMutators.id(2L)::accept);
    Task meeting = repository.save(ModelFactory
        .createWrapper(TaskType.MEETING)
        .with(TaskMutators.index(1))
        .with(TaskMutators.column(column))
        .getModel());
    ColumnMutators.tasks(job, meeting).accept(column);

    service.move(2L, 1L, null, 0);

    Assertions
        .assertThat(repository.findAll())
        .usingComparatorForType(ComparatorFactory.getComparator(Task.class), Task.class)
        .containsExactlyInAnyOrder(
            ModelFactory
                .createWrapper(TaskType.JOB)
                .with(TaskMutators.id(1L))
                .with(TaskMutators.index(1))
                .with(TaskMutators.column(ModelFactory
                    .createWrapper(ColumnType.MONDAY)
                    .with(ColumnMutators.id(1L))
                    .getModel()))
                .getModel(),
            ModelFactory
                .createWrapper(TaskType.MEETING)
                .with(TaskMutators.id(2L))
                .with(TaskMutators.index(0))
                .with(TaskMutators.column(ModelFactory
                    .createWrapper(ColumnType.MONDAY)
                    .with(ColumnMutators.id(1L))
                    .getModel()))
                .getModel()
        );
  }

  @Test
  public void move_whenNewColumnNotNull_expectMoveAtLastIndex() {
    Column monday = ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.id(1L))
        .getModel();
    Column wednesday = ModelFactory
        .createWrapper(ColumnType.WEDNESDAY)
        .with(ColumnMutators.id(2L))
        .getModel();
    identification.setStrategy(TaskMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createWrapper(TaskType.JOB)
        .with(TaskMutators.column(monday))
        .getModel());
    identification.setStrategy(TaskMutators.id(2L)::accept);
    Task meeting = repository.save(ModelFactory
        .createWrapper(TaskType.MEETING)
        .with(TaskMutators.column(wednesday))
        .getModel());
    ColumnMutators.tasks(meeting).accept(wednesday);

    Assertions
        .assertThat(service.move(1L, 1L, wednesday, null))
        .usingComparator(ComparatorFactory.getComparator(Task.class))
        .isEqualTo(ModelFactory
            .createWrapper(TaskType.JOB)
            .with(TaskMutators.id(1L))
            .with(TaskMutators.index(1))
            .with(TaskMutators.column(ModelFactory
                .createWrapper(ColumnType.WEDNESDAY)
                .with(ColumnMutators.id(2L))
                .getModel()))
            .getModel());
  }

  @Test
  public void move() {
    Column monday = ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.id(1L))
        .getModel();
    Column wednesday = ModelFactory
        .createWrapper(ColumnType.WEDNESDAY)
        .with(ColumnMutators.id(2L))
        .getModel();
    identification.setStrategy(TaskMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createWrapper(TaskType.JOB)
        .with(TaskMutators.column(monday))
        .getModel());
    identification.setStrategy(TaskMutators.id(2L)::accept);
    Task meeting = repository.save(ModelFactory
        .createWrapper(TaskType.MEETING)
        .with(TaskMutators.column(wednesday))
        .getModel());
    ColumnMutators.tasks(meeting).accept(wednesday);

    Assertions
        .assertThat(service.move(1L, 1L, wednesday, 0))
        .usingComparator(ComparatorFactory.getComparator(Task.class))
        .isEqualTo(ModelFactory
            .createWrapper(TaskType.JOB)
            .with(TaskMutators.id(1L))
            .with(TaskMutators.index(0))
            .with(TaskMutators.column(ModelFactory
                .createWrapper(ColumnType.WEDNESDAY)
                .with(ColumnMutators.id(2L))
                .getModel()))
            .getModel());
  }

  @Test
  public void find_whenNoEntityWithIdAndColumnId_expectException() {
    Assertions
        .assertThatThrownBy(() -> service.find(1L, 2L))
        .isExactlyInstanceOf(NotFoundException.class)
        .hasMessage("No task for id '1' and column id '2'");
  }

  @Test
  public void find() {
    Column column = ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.id(2L))
        .getModel();
    identification.setStrategy(TaskMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createWrapper(TaskType.JOB)
        .with(TaskMutators.column(column))
        .getModel());

    Assertions
        .assertThat(service.find(1L, 2L))
        .usingComparator(ComparatorFactory.getComparator(Task.class))
        .isEqualTo(ModelFactory
            .createWrapper(TaskType.JOB)
            .with(TaskMutators.id(1L))
            .with(TaskMutators.column(ModelFactory
                .createWrapper(ColumnType.MONDAY)
                .with(ColumnMutators.id(2L))
                .getModel()))
            .getModel());
  }

  @Test
  public void findAll() {
    Column column = ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.id(2L))
        .getModel();
    identification.setStrategy(TaskMutators.id(1L)::accept);
    repository.save(ModelFactory
        .createWrapper(TaskType.JOB)
        .with(TaskMutators.column(column))
        .getModel());
    identification.setStrategy(TaskMutators.id(2L)::accept);
    repository.save(ModelFactory
        .createWrapper(TaskType.MEETING)
        .with(TaskMutators.column(column))
        .getModel());

    Assertions
        .assertThat(service.findAll(2L, Pageable.unpaged()))
        .usingComparatorForType(ComparatorFactory.getComparator(Task.class), Task.class)
        .containsExactlyInAnyOrder(
            ModelFactory
                .createWrapper(TaskType.JOB)
                .with(TaskMutators.id(1L))
                .with(TaskMutators.column(ModelFactory
                    .createWrapper(ColumnType.MONDAY)
                    .with(ColumnMutators.id(2L))
                    .getModel()))
                .getModel(),
            ModelFactory
                .createWrapper(TaskType.MEETING)
                .with(TaskMutators.id(2L))
                .with(TaskMutators.column(ModelFactory
                    .createWrapper(ColumnType.MONDAY)
                    .with(ColumnMutators.id(2L))
                    .getModel()))
                .getModel()
        );
  }

  @Test
  public void delete_whenNoEntityWithIdAndColumnId_expectException() {
    Assertions
        .assertThatThrownBy(() -> service.delete(1L, 2L))
        .isExactlyInstanceOf(NotFoundException.class)
        .hasMessage("No task for id '1' and column id '2'");
  }

  @Test
  public void delete_whenLastEntity_expectChangeIndexTo0() {
    Column column = ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.id(2L))
        .getModel();
    identification.setStrategy(TaskMutators.id(1L)::accept);
    Task job = repository.save(ModelFactory
        .createWrapper(TaskType.JOB)
        .with(TaskMutators.column(column))
        .getModel());
    ColumnMutators.tasks(job).accept(column);

    service.delete(1L, 2L);

    Assertions
        .assertThat(repository.find(1L))
        .isNull();
  }

  @Test
  public void delete_expectChangeIndexToLast() {
    Column column = ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.id(2L))
        .getModel();
    identification.setStrategy(TaskMutators.id(1L)::accept);
    Task job = repository.save(ModelFactory
        .createWrapper(TaskType.JOB)
        .with(TaskMutators.index(0))
        .with(TaskMutators.column(column))
        .getModel());
    identification.setStrategy(TaskMutators.id(2L)::accept);
    Task meeting = repository.save(ModelFactory
        .createWrapper(TaskType.MEETING)
        .with(TaskMutators.index(1))
        .with(TaskMutators.column(column))
        .getModel());
    ColumnMutators.tasks(job, meeting).accept(column);

    service.delete(1L, 2L);

    Assertions
        .assertThat(repository.find(1L))
        .isNull();
    Assertions
        .assertThat(repository.find(2L))
        .usingComparator(ComparatorFactory.getComparator(Task.class))
        .isEqualTo(ModelFactory
            .createWrapper(TaskType.MEETING)
            .with(TaskMutators.id(2L))
            .with(TaskMutators.index(0))
            .with(TaskMutators.column(ModelFactory
                .createWrapper(ColumnType.MONDAY)
                .with(ColumnMutators.id(2L))
                .getModel()))
            .getModel());
  }

}
