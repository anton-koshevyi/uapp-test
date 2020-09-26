package com.uapp_llc.controller;

import javax.servlet.http.HttpServletResponse;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Project;
import com.uapp_llc.repository.ColumnRepository;
import com.uapp_llc.repository.ProjectRepository;
import com.uapp_llc.service.ColumnServiceImpl;
import com.uapp_llc.service.ProjectServiceImpl;
import com.uapp_llc.test.LazyInitBeanFactoryPostProcessor;
import com.uapp_llc.test.comparator.ComparatorFactory;
import com.uapp_llc.test.model.ModelFactoryProducer;
import com.uapp_llc.test.model.column.ColumnType;
import com.uapp_llc.test.model.project.ProjectType;
import com.uapp_llc.test.stub.repository.ColumnRepositoryStub;
import com.uapp_llc.test.stub.repository.ProjectRepositoryStub;
import com.uapp_llc.test.stub.repository.identification.IdentificationContext;

public class ColumnControllerTest {

  private IdentificationContext<Project> projectIdentification;
  private ProjectRepositoryStub projectRepository;
  private IdentificationContext<Column> columnIdentification;
  private ColumnRepositoryStub columnRepository;

  @BeforeEach
  public void setUp() {
    projectIdentification = new IdentificationContext<>();
    columnIdentification = new IdentificationContext<>();
    projectRepository = new ProjectRepositoryStub(projectIdentification);
    columnRepository = new ColumnRepositoryStub(columnIdentification);

    GenericApplicationContext appContext = new GenericApplicationContext();
    appContext.registerBean(ProjectRepository.class, () -> projectRepository);
    appContext.registerBean(ColumnRepository.class, () -> columnRepository);
    appContext.registerBean(ProjectServiceImpl.class);
    appContext.registerBean(ColumnServiceImpl.class);
    appContext.refresh();

    AnnotationConfigWebApplicationContext webContext =
        new AnnotationConfigWebApplicationContext();
    webContext.setParent(appContext);
    webContext.addBeanFactoryPostProcessor(new LazyInitBeanFactoryPostProcessor());
    webContext.setServletContext(new MockServletContext());
    webContext.register(WebMvcConfigurationSupport.class);
    webContext.register(ColumnController.class);
    webContext.refresh();

    RestAssuredMockMvc.mockMvc(MockMvcBuilders
        .webAppContextSetup(webContext)
        .alwaysDo(MockMvcResultHandlers.log())
        .build());
  }

  @Test
  @Disabled("Configure org.springframework.data.web.config.SpringDataWebConfiguration")
  public void getAll() throws JSONException {
    projectIdentification.setStrategy(e -> e.setId(1L));
    Project project = projectRepository.save(ModelFactoryProducer.getFactory(Project.class)
        .createModel(ProjectType.DEFAULT));
    columnIdentification.setStrategy(e -> e.setId(1L));
    columnRepository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY)
        .setProject(project));

    String response = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .when()
        .get("/projects/{projectId}/columns", 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString();
    String actual = new JSONObject(response)
        .getJSONArray("content")
        .toString();

    String expected = "[{"
        + "id: 1,"
        + "name: 'Monday tasks',"
        + "projectId: 1"
        + "}]";
    JSONAssert
        .assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
  }

  @Test
  public void create() throws JSONException {
    projectIdentification.setStrategy(e -> e.setId(1L));
    projectRepository.save(ModelFactoryProducer.getFactory(Project.class)
        .createModel(ProjectType.DEFAULT));
    columnIdentification.setStrategy(e -> e.setId(1L));

    String actual = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .body("{ \"name\": \"Monday tasks\" }")
        .when()
        .post("/projects/{projectId}/columns", 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString();

    String expected = "{"
        + "id: 1,"
        + "name: 'Monday tasks',"
        + "index: 0,"
        + "projectId: 1"
        + "}";
    JSONAssert
        .assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
  }

  @Test
  public void get() throws JSONException {
    projectIdentification.setStrategy(e -> e.setId(1L));
    Project project = projectRepository.save(ModelFactoryProducer.getFactory(Project.class)
        .createModel(ProjectType.DEFAULT));
    columnIdentification.setStrategy(e -> e.setId(1L));
    columnRepository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY)
        .setProject(project));

    String actual = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .when()
        .get("/projects/{projectId}/columns/{id}", 1, 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString();

    String expected = "{"
        + "id: 1,"
        + "name: 'Monday tasks',"
        + "index: 0,"
        + "projectId: 1"
        + "}";
    JSONAssert
        .assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
  }

  @Test
  public void update() throws JSONException {
    projectIdentification.setStrategy(e -> e.setId(1L));
    Project project = projectRepository.save(ModelFactoryProducer.getFactory(Project.class)
        .createModel(ProjectType.DEFAULT));
    columnIdentification.setStrategy(e -> e.setId(1L));
    columnRepository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY)
        .setProject(project));

    String actual = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .body("{ \"name\": \"Tuesday tasks\" }")
        .when()
        .patch("/projects/{projectId}/columns/{id}", 1, 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString();

    String expected = "{"
        + "id: 1,"
        + "name: 'Tuesday tasks',"
        + "index: 0,"
        + "projectId: 1"
        + "}";
    JSONAssert
        .assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
  }

  @Test
  public void changeIndex() {
    projectIdentification.setStrategy(e -> e.setId(1L));
    Project project = projectRepository.save(ModelFactoryProducer.getFactory(Project.class)
        .createModel(ProjectType.DEFAULT));
    columnIdentification.setStrategy(e -> e.setId(1L));
    Column monday = columnRepository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY)
        .setIndex(0)
        .setProject(project));
    columnIdentification.setStrategy(e -> e.setId(2L));
    Column wednesday = columnRepository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.WEDNESDAY)
        .setIndex(1)
        .setProject(project));
    project.setColumns(Lists.newArrayList(monday, wednesday));

    RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .body("{ \"newIndex\": 1 }")
        .when()
        .put("/projects/{projectId}/columns/{id}", 1, 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK);

    Assertions
        .assertThat(columnRepository.findAll())
        .usingComparatorForType(ComparatorFactory.getComparator(Column.class), Column.class)
        .containsExactlyInAnyOrder(
            ModelFactoryProducer.getFactory(Column.class)
                .createModel(ColumnType.MONDAY)
                .setId(1L)
                .setIndex(1)
                .setProject(ModelFactoryProducer.getFactory(Project.class)
                    .createModel(ProjectType.DEFAULT)
                    .setId(1L)),
            ModelFactoryProducer.getFactory(Column.class)
                .createModel(ColumnType.WEDNESDAY)
                .setId(2L)
                .setIndex(0)
                .setProject(ModelFactoryProducer.getFactory(Project.class)
                    .createModel(ProjectType.DEFAULT)
                    .setId(1L))
        );
  }

  @Test
  public void delete() {
    projectIdentification.setStrategy(e -> e.setId(1L));
    Project project = projectRepository.save(ModelFactoryProducer.getFactory(Project.class)
        .createModel(ProjectType.DEFAULT));
    columnIdentification.setStrategy(e -> e.setId(1L));
    columnRepository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY)
        .setProject(project));

    RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .when()
        .delete("/projects/{projectId}/columns/{id}", 1, 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK);

    Assertions
        .assertThat(columnRepository.find(1L))
        .isNull();
  }

}
