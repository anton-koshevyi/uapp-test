package com.uapp_llc.service;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import com.uapp_llc.exception.IllegalActionException;
import com.uapp_llc.exception.NotFoundException;
import com.uapp_llc.model.Column;
import com.uapp_llc.model.Project;
import com.uapp_llc.test.comparator.ComparatorFactory;
import com.uapp_llc.test.model.ModelFactoryProducer;
import com.uapp_llc.test.model.column.ColumnType;
import com.uapp_llc.test.model.project.ProjectType;
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
    Project project = ModelFactoryProducer.getFactory(Project.class)
        .createModel(ProjectType.DEFAULT)
        .setId(2L);

    service.create(
        project,
        "Monday tasks"
    );

    Assertions
        .assertThat(repository.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Column.class))
        .isEqualTo(new Column()
            .setId(1L)
            .setName("Monday tasks")
            .setProject(ModelFactoryProducer.getFactory(Project.class)
                .createModel(ProjectType.DEFAULT)
                .setId(2L)));
  }

  @Test
  public void update_whenNoEntityWithIdAndProjectId_expectException() {
    Assertions
        .assertThatThrownBy(() -> service.update(1L, 2L, "Tuesday tasks"))
        .isExactlyInstanceOf(NotFoundException.class)
        .hasFieldOrPropertyWithValue("getCodes",
            new Object[]{"notFound.column.byIdAndProjectId"})
        .hasFieldOrPropertyWithValue("getArguments", new Object[]{1L, 2L});
  }

  @Test
  public void update() {
    identification.setStrategy(e -> e.setId(1L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY)
        .setProject(ModelFactoryProducer.getFactory(Project.class)
            .createModel(ProjectType.DEFAULT)
            .setId(2L)));

    service.update(
        1L,
        2L,
        "Tuesday tasks"
    );

    Assertions
        .assertThat(repository.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Column.class))
        .isEqualTo(ModelFactoryProducer.getFactory(Column.class)
            .createModel(ColumnType.MONDAY)
            .setId(1L)
            .setName("Tuesday tasks")
            .setProject(ModelFactoryProducer.getFactory(Project.class)
                .createModel(ProjectType.DEFAULT)
                .setId(2L)));
  }

  @Test
  public void changeIndex_whenNoEntityWithIdAndProjectId_expectException() {
    Assertions
        .assertThatThrownBy(() -> service.changeIndex(2L, 1L, 0))
        .isExactlyInstanceOf(NotFoundException.class)
        .hasFieldOrPropertyWithValue("getCodes",
            new Object[]{"notFound.column.byIdAndProjectId"})
        .hasFieldOrPropertyWithValue("getArguments", new Object[]{2L, 1L});
  }

  @Test
  public void changeIndex_whenIndexOutOfBounds_expectException() {
    Project project = ModelFactoryProducer.getFactory(Project.class)
        .createModel(ProjectType.DEFAULT)
        .setId(1L);
    identification.setStrategy(e -> e.setId(1L));
    Column monday = repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY)
        .setProject(project));
    identification.setStrategy(e -> e.setId(2L));
    Column wednesday = repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.WEDNESDAY)
        .setProject(project));
    project.setColumns(Lists.newArrayList(monday, wednesday));

    Assertions
        .assertThatThrownBy(() -> service.changeIndex(2L, 1L, -1))
        .isExactlyInstanceOf(IllegalActionException.class)
        .hasFieldOrPropertyWithValue("getCodes",
            new Object[]{"illegalAction.column.indexOutOfBounds"})
        .hasFieldOrPropertyWithValue("getArguments", new Object[]{-1});
  }

  @Test
  public void changeIndex() {
    Project project = ModelFactoryProducer.getFactory(Project.class)
        .createModel(ProjectType.DEFAULT)
        .setId(1L);
    identification.setStrategy(e -> e.setId(1L));
    Column monday = repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY)
        .setProject(project));
    identification.setStrategy(e -> e.setId(2L));
    Column wednesday = repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.WEDNESDAY)
        .setProject(project));
    project.setColumns(Lists.newArrayList(monday, wednesday));

    service.changeIndex(2L, 1L, 0);

    Assertions
        .assertThat(project.getColumns())
        .usingComparatorForType(ComparatorFactory.getComparator(Column.class), Column.class)
        .containsExactly(
            ModelFactoryProducer.getFactory(Column.class)
                .createModel(ColumnType.WEDNESDAY)
                .setId(2L)
                .setProject(ModelFactoryProducer.getFactory(Project.class)
                    .createModel(ProjectType.DEFAULT)
                    .setId(1L)),
            ModelFactoryProducer.getFactory(Column.class)
                .createModel(ColumnType.MONDAY)
                .setId(1L)
                .setProject(ModelFactoryProducer.getFactory(Project.class)
                    .createModel(ProjectType.DEFAULT)
                    .setId(1L))
        );
  }

  @Test
  public void find_whenNoEntityWithIdAndProjectId_expectException() {
    Assertions
        .assertThatThrownBy(() -> service.find(1L, 2L))
        .isExactlyInstanceOf(NotFoundException.class)
        .hasFieldOrPropertyWithValue("getCodes",
            new Object[]{"notFound.column.byIdAndProjectId"})
        .hasFieldOrPropertyWithValue("getArguments", new Object[]{1L, 2L});
  }

  @Test
  public void find() {
    identification.setStrategy(e -> e.setId(1L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY)
        .setProject(ModelFactoryProducer.getFactory(Project.class)
            .createModel(ProjectType.DEFAULT)
            .setId(2L)));

    Assertions
        .assertThat(service.find(1L, 2L))
        .usingComparator(ComparatorFactory.getComparator(Column.class))
        .isEqualTo(ModelFactoryProducer.getFactory(Column.class)
            .createModel(ColumnType.MONDAY)
            .setId(1L)
            .setProject(ModelFactoryProducer.getFactory(Project.class)
                .createModel(ProjectType.DEFAULT)
                .setId(2L)));
  }

  @Test
  public void findAll() {
    Project project = ModelFactoryProducer.getFactory(Project.class)
        .createModel(ProjectType.DEFAULT)
        .setId(2L);
    identification.setStrategy(e -> e.setId(1L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY)
        .setProject(project));
    identification.setStrategy(e -> e.setId(2L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.WEDNESDAY)
        .setProject(project));

    Assertions
        .assertThat(service.findAll(2L, Pageable.unpaged()))
        .usingComparatorForType(ComparatorFactory.getComparator(Column.class), Column.class)
        .containsExactlyInAnyOrder(
            ModelFactoryProducer.getFactory(Column.class)
                .createModel(ColumnType.MONDAY)
                .setId(1L)
                .setProject(ModelFactoryProducer.getFactory(Project.class)
                    .createModel(ProjectType.DEFAULT)
                    .setId(2L)),
            ModelFactoryProducer.getFactory(Column.class)
                .createModel(ColumnType.WEDNESDAY)
                .setId(2L)
                .setProject(ModelFactoryProducer.getFactory(Project.class)
                    .createModel(ProjectType.DEFAULT)
                    .setId(2L))
        );
  }

  @Test
  public void delete_whenNoEntityWithIdAndProjectId_expectException() {
    Assertions
        .assertThatThrownBy(() -> service.delete(1L, 2L))
        .isExactlyInstanceOf(NotFoundException.class)
        .hasFieldOrPropertyWithValue("getCodes",
            new Object[]{"notFound.column.byIdAndProjectId"})
        .hasFieldOrPropertyWithValue("getArguments", new Object[]{1L, 2L});
  }

  @Test
  public void delete() {
    Project project = ModelFactoryProducer.getFactory(Project.class)
        .createModel(ProjectType.DEFAULT)
        .setId(2L);
    identification.setStrategy(e -> e.setId(1L));
    repository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY)
        .setProject(project));

    service.delete(1L, 2L);

    Assertions
        .assertThat(repository.find(1L))
        .isNull();
  }

}
