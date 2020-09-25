package com.uapp_llc.controller;

import javax.servlet.http.HttpServletResponse;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.uapp_llc.model.Project;
import com.uapp_llc.repository.ProjectRepository;
import com.uapp_llc.service.ProjectServiceImpl;
import com.uapp_llc.test.LazyInitBeanFactoryPostProcessor;
import com.uapp_llc.test.comparator.ComparatorFactory;
import com.uapp_llc.test.model.ModelFactoryProducer;
import com.uapp_llc.test.model.project.ProjectType;
import com.uapp_llc.test.stub.repository.ProjectRepositoryStub;
import com.uapp_llc.test.stub.repository.identification.IdentificationContext;

public class ProjectControllerTest {

  private IdentificationContext<Project> identification;
  private ProjectRepositoryStub repository;

  @BeforeEach
  public void setUp() {
    identification = new IdentificationContext<>();
    repository = new ProjectRepositoryStub(identification);

    GenericApplicationContext appContext = new GenericApplicationContext();
    appContext.registerBean(ProjectRepository.class, () -> repository);
    appContext.registerBean(ProjectServiceImpl.class);
    appContext.refresh();

    AnnotationConfigWebApplicationContext webContext =
        new AnnotationConfigWebApplicationContext();
    webContext.setParent(appContext);
    webContext.addBeanFactoryPostProcessor(new LazyInitBeanFactoryPostProcessor());
    webContext.setServletContext(new MockServletContext());
    webContext.register(WebMvcConfigurationSupport.class);
    webContext.register(ProjectController.class);
    webContext.refresh();

    RestAssuredMockMvc.mockMvc(MockMvcBuilders
        .webAppContextSetup(webContext)
        .alwaysDo(MockMvcResultHandlers.log())
        .build());
  }

  @Test
  public void create() {
    identification.setStrategy(e -> e.setId(1L));

    RestAssuredMockMvc
        .post("/projects")
        .then()
        .statusCode(HttpServletResponse.SC_OK);

    Assertions
        .assertThat(repository.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Project.class))
        .isEqualTo(new Project()
            .setId(1L));
  }

  @Test
  public void delete() {
    identification.setStrategy(e -> e.setId(1L));
    repository.save(ModelFactoryProducer.getFactory(Project.class)
        .createModel(ProjectType.DEFAULT));

    RestAssuredMockMvc
        .delete("/projects/{id}", 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK);

    Assertions
        .assertThat(repository.find(1L))
        .isNull();
  }

}
