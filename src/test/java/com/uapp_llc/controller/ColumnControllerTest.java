package com.uapp_llc.controller;

import javax.servlet.http.HttpServletResponse;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.uapp_llc.model.Column;
import com.uapp_llc.repository.ColumnRepository;
import com.uapp_llc.service.ColumnServiceImpl;
import com.uapp_llc.test.LazyInitBeanFactoryPostProcessor;
import com.uapp_llc.test.model.factory.ModelFactory;
import com.uapp_llc.test.model.mutator.ColumnMutators;
import com.uapp_llc.test.model.type.ColumnType;
import com.uapp_llc.test.stub.repository.ColumnRepositoryStub;
import com.uapp_llc.test.stub.repository.identification.IdentificationContext;

public class ColumnControllerTest {

  private IdentificationContext<Column> columnIdentification;
  private ColumnRepositoryStub columnRepository;

  @BeforeEach
  public void setUp() {
    columnIdentification = new IdentificationContext<>();
    columnRepository = new ColumnRepositoryStub(columnIdentification);

    GenericApplicationContext appContext = new GenericApplicationContext();
    appContext.registerBean(ColumnRepository.class, () -> columnRepository);
    appContext.registerBean(ColumnServiceImpl.class);
    appContext.refresh();

    AnnotationConfigWebApplicationContext webContext =
        new AnnotationConfigWebApplicationContext();
    webContext.setParent(appContext);
    webContext.addBeanFactoryPostProcessor(new LazyInitBeanFactoryPostProcessor());
    webContext.setServletContext(new MockServletContext());
    webContext.register(TestConfig.class);
    webContext.register(ColumnController.class);
    webContext.refresh();

    RestAssuredMockMvc.mockMvc(MockMvcBuilders
        .webAppContextSetup(webContext)
        .alwaysDo(MockMvcResultHandlers.log())
        .build());
  }

  @Test
  public void getAll() throws JSONException {
    columnIdentification.setStrategy(ColumnMutators.id(1L)::accept);
    columnRepository.save(ModelFactory
        .createModel(ColumnType.MONDAY));

    String response = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .when()
        .get("/columns")
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
        + "index: 0"
        + "}]";
    JSONAssert
        .assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
  }

  @Test
  public void create() throws JSONException {
    columnIdentification.setStrategy(ColumnMutators.id(1L)::accept);

    String actual = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .body("{ \"name\": \"Monday tasks\" }")
        .when()
        .post("/columns")
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString();

    String expected = "{"
        + "id: 1,"
        + "name: 'Monday tasks',"
        + "index: 0"
        + "}";
    JSONAssert
        .assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
  }

  @Test
  public void get() throws JSONException {
    columnIdentification.setStrategy(ColumnMutators.id(1L)::accept);
    columnRepository.save(ModelFactory
        .createModel(ColumnType.MONDAY));

    String actual = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .when()
        .get("/columns/{id}", 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString();

    String expected = "{"
        + "id: 1,"
        + "name: 'Monday tasks',"
        + "index: 0"
        + "}";
    JSONAssert
        .assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
  }

  @Test
  public void update() throws JSONException {
    columnIdentification.setStrategy(ColumnMutators.id(1L)::accept);
    columnRepository.save(ModelFactory
        .createModel(ColumnType.MONDAY));

    String actual = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .body("{ \"name\": \"Tuesday tasks\" }")
        .when()
        .patch("/columns/{id}", 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString();

    String expected = "{"
        + "id: 1,"
        + "name: 'Tuesday tasks',"
        + "index: 0"
        + "}";
    JSONAssert
        .assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
  }

  @Test
  public void changeIndex() throws JSONException {
    columnIdentification.setStrategy(ColumnMutators.id(1L)::accept);
    columnRepository.save(ModelFactory
        .createWrapper(ColumnType.MONDAY)
        .with(ColumnMutators.index(0))
        .getModel());
    columnIdentification.setStrategy(ColumnMutators.id(2L)::accept);
    columnRepository.save(ModelFactory
        .createWrapper(ColumnType.WEDNESDAY)
        .with(ColumnMutators.index(1))
        .getModel());

    String actual = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .body("{ \"newIndex\": 1 }")
        .when()
        .put("/columns/{id}", 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString();

    String expected = "{"
        + "id: 1,"
        + "name: 'Monday tasks',"
        + "index: 1"
        + "}";
    JSONAssert
        .assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
  }

  @Test
  public void delete() {
    columnIdentification.setStrategy(ColumnMutators.id(1L)::accept);
    columnRepository.save(ModelFactory
        .createModel(ColumnType.MONDAY));

    RestAssuredMockMvc
        .delete("/columns/{id}", 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK);

    Assertions
        .assertThat(columnRepository.find(1L))
        .isNull();
  }


  @EnableWebMvc
  @EnableSpringDataWebSupport
  private static class TestConfig {
  }

}
