package com.uapp_llc.controller

import com.uapp_llc.model.Column
import com.uapp_llc.service.ColumnServiceImpl
import com.uapp_llc.test.LazyInitBeanFactoryPostProcessor
import com.uapp_llc.test.model.factory.ModelFactory
import com.uapp_llc.test.model.type.ColumnType
import com.uapp_llc.test.stub.repository.ColumnRepositoryStub
import com.uapp_llc.test.stub.repository.identification.IdentificationContext
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.assertj.core.api.Assertions
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.context.support.GenericApplicationContext
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.mock.web.MockServletContext
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import javax.servlet.http.HttpServletResponse

class ColumnControllerTest {

  private lateinit var columnIdentification: IdentificationContext<Column>
  private lateinit var columnRepository: ColumnRepositoryStub

  @BeforeEach
  fun setUp() {
    columnIdentification = IdentificationContext()
    columnRepository = ColumnRepositoryStub(columnIdentification)

    val appContext: GenericApplicationContext =
        GenericApplicationContext().apply {
          registerBean(ColumnServiceImpl::class.java, columnRepository)
          refresh()
        }

    val webContext: AnnotationConfigWebApplicationContext =
        AnnotationConfigWebApplicationContext().apply {
          parent = appContext
          servletContext = MockServletContext()
          addBeanFactoryPostProcessor(LazyInitBeanFactoryPostProcessor())
          register(TestConfig::class.java)
          register(ColumnController::class.java)
          refresh()
        }

    RestAssuredMockMvc.mockMvc(MockMvcBuilders
        .webAppContextSetup(webContext)
        .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.log())
        .build())
  }

  @Test
  fun getAll() {
    columnIdentification.setStrategy { it.id = 1L }
    columnRepository.save(ModelFactory
        .createModel(ColumnType.MONDAY))

    val response: String = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .`when`()
        .get("/columns")
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString()
    val actual: String = JSONObject(response)
        .getJSONArray("content")
        .toString()

    val expected =
        """
        [{
          id: 1,
          name: 'Monday tasks',
          index: 0
        }]  
        """
    JSONAssert
        .assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE)
  }

  @Test
  fun create() {
    columnIdentification.setStrategy { it.id = 1L }

    val actual: String = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .body("{ \"name\": \"Monday tasks\" }")
        .`when`()
        .post("/columns")
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString()

    val expected =
        """
        {
          id: 1,
          name: 'Monday tasks',
          index: 0
        }  
        """
    JSONAssert
        .assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE)
  }

  @Test
  fun get() {
    columnIdentification.setStrategy { it.id = 1L }
    columnRepository.save(ModelFactory
        .createModel(ColumnType.MONDAY))

    val actual: String = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .`when`()
        .get("/columns/{id}", 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString()

    val expected =
        """
        {
          id: 1,
          name: 'Monday tasks',
          index: 0
        }  
        """
    JSONAssert
        .assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE)
  }

  @Test
  fun update() {
    columnIdentification.setStrategy { it.id = 1L }
    columnRepository.save(ModelFactory
        .createModel(ColumnType.MONDAY))

    val actual: String = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .body("{ \"name\": \"Tuesday tasks\" }")
        .`when`()
        .patch("/columns/{id}", 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString()

    val expected =
        """
        {
          id: 1,
          name: 'Tuesday tasks',
          index: 0
        }  
        """
    JSONAssert
        .assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE)
  }

  @Test
  fun changeIndex() {
    columnIdentification.setStrategy { it.id = 1L }
    columnRepository.save(ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.index = 0 })
    columnIdentification.setStrategy { it.id = 2L }
    columnRepository.save(ModelFactory
        .createModel(ColumnType.WEDNESDAY)
        .also { it.index = 1 })

    val actual: String = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .body("{ \"newIndex\": 1 }")
        .`when`()
        .put("/columns/{id}", 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString()

    val expected =
        """
        {
          id: 1,
          name: 'Monday tasks',
          index: 1
        }  
        """
    JSONAssert
        .assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE)
  }

  @Test
  fun delete() {
    columnIdentification.setStrategy { it.id = 1L }
    columnRepository.save(ModelFactory
        .createModel(ColumnType.MONDAY))

    RestAssuredMockMvc
        .delete("/columns/{id}", 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)

    Assertions
        .assertThat(columnRepository.find(1L))
        .isNull()
  }


  @EnableWebMvc
  @EnableSpringDataWebSupport
  private class TestConfig

}
