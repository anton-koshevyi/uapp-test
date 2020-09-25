package com.uapp_llc.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uapp_llc.exception.NotFoundException;
import com.uapp_llc.model.Project;
import com.uapp_llc.test.comparator.ComparatorFactory;
import com.uapp_llc.test.model.ModelFactoryProducer;
import com.uapp_llc.test.model.project.ProjectType;
import com.uapp_llc.test.stub.repository.ProjectRepositoryStub;
import com.uapp_llc.test.stub.repository.identification.IdentificationContext;

public class ProjectServiceTest {

  private IdentificationContext<Project> identification;
  private ProjectRepositoryStub repository;
  private ProjectService service;

  @BeforeEach
  public void setUp() {
    identification = new IdentificationContext<>();
    repository = new ProjectRepositoryStub(identification);
    service = new ProjectServiceImpl(repository);
  }

  @Test
  public void create() {
    identification.setStrategy(e -> e.setId(1L));

    service.create();

    Assertions
        .assertThat(repository.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Project.class))
        .isEqualTo(new Project()
            .setId(1L));
  }

  @Test
  public void find_whenNoEntityWithId_expectException() {
    Assertions
        .assertThatThrownBy(() -> service.find(1L))
        .isExactlyInstanceOf(NotFoundException.class)
        .hasFieldOrPropertyWithValue("getCodes", new Object[]{"notFound.project.byId"})
        .hasFieldOrPropertyWithValue("getArguments", new Object[]{1L});
  }

  @Test
  public void find() {
    identification.setStrategy(e -> e.setId(1L));
    repository.save(ModelFactoryProducer.getFactory(Project.class)
        .createModel(ProjectType.DEFAULT));

    Assertions
        .assertThat(service.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Project.class))
        .isEqualTo(ModelFactoryProducer.getFactory(Project.class)
            .createModel(ProjectType.DEFAULT)
            .setId(1L));
  }

  @Test
  public void delete_whenNoEntityWithId_expectException() {
    Assertions
        .assertThatThrownBy(() -> service.delete(1L))
        .isExactlyInstanceOf(NotFoundException.class)
        .hasFieldOrPropertyWithValue("getCodes", new Object[]{"notFound.project.byId"})
        .hasFieldOrPropertyWithValue("getArguments", new Object[]{1L});

  }

  @Test
  public void delete() {
    identification.setStrategy(e -> e.setId(1L));
    repository.save(ModelFactoryProducer.getFactory(Project.class)
        .createModel(ProjectType.DEFAULT));

    service.delete(1L);

    Assertions
        .assertThat(repository.find(1L))
        .isNull();
  }

}
