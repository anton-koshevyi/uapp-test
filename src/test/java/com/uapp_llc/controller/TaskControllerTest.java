package com.uapp_llc.controller;

import javax.servlet.http.HttpServletResponse;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.uapp_llc.model.Column;
import com.uapp_llc.model.Task;
import com.uapp_llc.repository.ColumnRepository;
import com.uapp_llc.repository.TaskRepository;
import com.uapp_llc.service.ColumnServiceImpl;
import com.uapp_llc.service.TaskServiceImpl;
import com.uapp_llc.test.LazyInitBeanFactoryPostProcessor;
import com.uapp_llc.test.model.ModelFactoryProducer;
import com.uapp_llc.test.model.column.ColumnType;
import com.uapp_llc.test.model.task.TaskType;
import com.uapp_llc.test.stub.repository.ColumnRepositoryStub;
import com.uapp_llc.test.stub.repository.TaskRepositoryStub;
import com.uapp_llc.test.stub.repository.identification.IdentificationContext;

public class TaskControllerTest {

  private IdentificationContext<Column> columnIdentification;
  private ColumnRepositoryStub columnRepository;
  private IdentificationContext<Task> taskIdentification;
  private TaskRepositoryStub taskRepository;

  @BeforeEach
  public void setUp() {
    columnIdentification = new IdentificationContext<>();
    columnRepository = new ColumnRepositoryStub(columnIdentification);
    taskIdentification = new IdentificationContext<>();
    taskRepository = new TaskRepositoryStub(taskIdentification);

    GenericApplicationContext appContext = new GenericApplicationContext();
    appContext.registerBean(ColumnRepository.class, () -> columnRepository);
    appContext.registerBean(ColumnServiceImpl.class);
    appContext.registerBean(TaskRepository.class, () -> taskRepository);
    appContext.registerBean(TaskServiceImpl.class);
    appContext.refresh();

    AnnotationConfigWebApplicationContext webContext =
        new AnnotationConfigWebApplicationContext();
    webContext.setParent(appContext);
    webContext.addBeanFactoryPostProcessor(new LazyInitBeanFactoryPostProcessor());
    webContext.setServletContext(new MockServletContext());
    webContext.register(WebMvcConfigurationSupport.class);
    webContext.register(SpringDataWebConfiguration.class);
    webContext.register(TaskController.class);
    webContext.refresh();

    RestAssuredMockMvc.mockMvc(MockMvcBuilders
        .webAppContextSetup(webContext)
        .alwaysDo(MockMvcResultHandlers.log())
        .build());
  }

  @Test
  public void getAll() throws JSONException {
    columnIdentification.setStrategy(e -> e.setId(1L));
    Column column = columnRepository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY));
    taskIdentification.setStrategy(e -> e.setId(1L));
    taskRepository.save(ModelFactoryProducer.getFactory(Task.class)
        .createModel(TaskType.JOB)
        .setColumn(column));

    String response = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .when()
        .get("/columns/{columnId}/tasks", 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString();
    String actual = new JSONObject(response)
        .getJSONArray("content")
        .toString();

    String expected = "[{"
        + "id: 1,"
        + "createdAt: (customized),"
        + "name: 'Job',"
        + "description: 'Go to job',"
        + "index: 0,"
        + "column: {"
        + "  id: 1,"
        + "  name: 'Monday tasks',"
        + "  index: 0"
        + "}"
        + "}]";
    // For some reason fails when not strict array ordering
    JSONAssert
        .assertEquals(expected, actual, new CustomComparator(JSONCompareMode.STRICT,
            new Customization("[*].createdAt", (act, exp) -> act != null)
        ));
  }

  @Test
  public void create() throws JSONException {
    columnIdentification.setStrategy(e -> e.setId(1L));
    columnRepository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY));
    taskIdentification.setStrategy(e -> e.setId(1L));

    String actual = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .body("{"
            + "\"name\": \"Job\","
            + "\"description\": \"Go to job\""
            + "}")
        .when()
        .post("/columns/{columnId}/tasks", 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString();

    String expected = "{"
        + "id: 1,"
        + "createdAt: (customized),"
        + "name: 'Job',"
        + "description: 'Go to job',"
        + "index: 0,"
        + "column: {"
        + "  id: 1,"
        + "  name: 'Monday tasks',"
        + "  index: 0"
        + "}"
        + "}";
    JSONAssert
        .assertEquals(expected, actual, new CustomComparator(JSONCompareMode.NON_EXTENSIBLE,
            new Customization("createdAt", (act, exp) -> act != null)
        ));
  }

  @Test
  public void get() throws JSONException {
    columnIdentification.setStrategy(e -> e.setId(1L));
    Column column = columnRepository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY));
    taskIdentification.setStrategy(e -> e.setId(1L));
    taskRepository.save(ModelFactoryProducer.getFactory(Task.class)
        .createModel(TaskType.JOB)
        .setColumn(column));

    String actual = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .when()
        .get("/columns/{columnId}/tasks/{id}", 1, 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString();

    String expected = "{"
        + "id: 1,"
        + "createdAt: (customized),"
        + "name: 'Job',"
        + "description: 'Go to job',"
        + "index: 0,"
        + "column: {"
        + "  id: 1,"
        + "  name: 'Monday tasks',"
        + "  index: 0"
        + "}"
        + "}";
    JSONAssert
        .assertEquals(expected, actual, new CustomComparator(JSONCompareMode.NON_EXTENSIBLE,
            new Customization("createdAt", (act, exp) -> act != null)
        ));
  }

  @Test
  public void update() throws JSONException {
    columnIdentification.setStrategy(e -> e.setId(1L));
    Column column = columnRepository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY));
    taskIdentification.setStrategy(e -> e.setId(1L));
    taskRepository.save(ModelFactoryProducer.getFactory(Task.class)
        .createModel(TaskType.JOB)
        .setColumn(column));

    String actual = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .body("{"
            + "\"name\": \"Meeting\","
            + "\"description\": \"Meet John\""
            + "}")
        .when()
        .patch("/columns/{columnId}/tasks/{id}", 1, 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString();

    String expected = "{"
        + "id: 1,"
        + "createdAt: (customized),"
        + "name: 'Meeting',"
        + "description: 'Meet John',"
        + "index: 0,"
        + "column: {"
        + "  id: 1,"
        + "  name: 'Monday tasks',"
        + "  index: 0"
        + "}"
        + "}";
    JSONAssert
        .assertEquals(expected, actual, new CustomComparator(JSONCompareMode.NON_EXTENSIBLE,
            new Customization("createdAt", (act, exp) -> act != null)
        ));
  }

  @Test
  public void move() throws JSONException {
    columnIdentification.setStrategy(e -> e.setId(1L));
    Column monday = columnRepository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY));
    columnIdentification.setStrategy(e -> e.setId(2L));
    Column wednesday = columnRepository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.WEDNESDAY));
    taskIdentification.setStrategy(e -> e.setId(1L));
    Task job = taskRepository.save(ModelFactoryProducer.getFactory(Task.class)
        .createModel(TaskType.JOB)
        .setColumn(monday));
    taskIdentification.setStrategy(e -> e.setId(2L));
    Task meeting = taskRepository.save(ModelFactoryProducer.getFactory(Task.class)
        .createModel(TaskType.MEETING)
        .setColumn(wednesday));
    monday.setTasks(Lists.newArrayList(job));
    wednesday.setTasks(Lists.newArrayList(meeting));

    String actual = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .body("{"
            + "\"newIndex\": \"0\","
            + "\"newColumnId\": \"2\""
            + "}")
        .when()
        .put("/columns/{columnId}/tasks/{id}", 1, 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString();

    String expected = "{"
        + "id: 1,"
        + "createdAt: (customized),"
        + "name: 'Job',"
        + "description: 'Go to job',"
        + "index: 0,"
        + "column: {"
        + "  id: 2,"
        + "  name: 'Wednesday tasks',"
        + "  index: 1"
        + "}"
        + "}";
    JSONAssert
        .assertEquals(expected, actual, new CustomComparator(JSONCompareMode.NON_EXTENSIBLE,
            new Customization("createdAt", (act, exp) -> act != null)
        ));
  }

  @Test
  public void delete() {
    columnIdentification.setStrategy(e -> e.setId(1L));
    Column column = columnRepository.save(ModelFactoryProducer.getFactory(Column.class)
        .createModel(ColumnType.MONDAY));
    taskIdentification.setStrategy(e -> e.setId(1L));
    taskRepository.save(ModelFactoryProducer.getFactory(Task.class)
        .createModel(TaskType.JOB)
        .setColumn(column));

    RestAssuredMockMvc
        .delete("/columns/{columnId}/tasks/{id}", 1, 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK);

    Assertions
        .assertThat(taskRepository.find(1L))
        .isNull();
  }

}
