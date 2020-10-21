package com.uapp_llc.controller

import javax.servlet.http.HttpServletResponse
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.assertj.core.api.Assertions
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.Customization
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.comparator.CustomComparator
import org.springframework.context.support.GenericApplicationContext
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.mock.web.MockServletContext
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import com.uapp_llc.model.Column
import com.uapp_llc.model.Task
import com.uapp_llc.service.ColumnServiceImpl
import com.uapp_llc.service.TaskServiceImpl
import com.uapp_llc.test.LazyInitBeanFactoryPostProcessor
import com.uapp_llc.test.model.factory.ModelFactory
import com.uapp_llc.test.model.type.ColumnType
import com.uapp_llc.test.model.type.TaskType
import com.uapp_llc.test.stub.repository.ColumnRepositoryStub
import com.uapp_llc.test.stub.repository.TaskRepositoryStub
import com.uapp_llc.test.stub.repository.identification.IdentificationContext

class TaskControllerTest {

  private lateinit var columnIdentification: IdentificationContext<Column>
  private lateinit var columnRepository: ColumnRepositoryStub
  private lateinit var taskIdentification: IdentificationContext<Task>
  private lateinit var taskRepository: TaskRepositoryStub

  @BeforeEach
  fun setUp() {
    columnIdentification = IdentificationContext()
    columnRepository = ColumnRepositoryStub(columnIdentification)
    taskIdentification = IdentificationContext()
    taskRepository = TaskRepositoryStub(taskIdentification)

    val appContext: GenericApplicationContext =
        GenericApplicationContext().apply {
          registerBean(ColumnServiceImpl::class.java, columnRepository)
          registerBean(TaskServiceImpl::class.java, taskRepository)
          refresh()
        }

    val webContext: AnnotationConfigWebApplicationContext =
        AnnotationConfigWebApplicationContext().apply {
          parent = appContext
          servletContext = MockServletContext()
          addBeanFactoryPostProcessor(LazyInitBeanFactoryPostProcessor())
          register(TestConfig::class.java)
          register(TaskController::class.java)
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
    val column: Column = columnRepository.save(ModelFactory
        .createModel(ColumnType.MONDAY))
    taskIdentification.setStrategy { it.id = 1L }
    taskRepository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also { it.column = column })

    val response = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .`when`()
        .get("/columns/{columnId}/tasks", 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString()
    val actual = JSONObject(response)
        .getJSONArray("content")
        .toString()

    val expected =
        """
        [{
          id: 1,
          createdAt: (customized),
          name: 'Job',
          description: 'Go to job',
          index: 0,
          column: {
            id: 1,
            name: 'Monday tasks',
            index: 0
          }
        }]  
        """
    // For some reason fails when not strict array ordering
    JSONAssert
        .assertEquals(expected, actual, CustomComparator(JSONCompareMode.STRICT,
            Customization("[*].createdAt") { act, _ -> act != null }
        ))
  }

  @Test
  fun create() {
    columnIdentification.setStrategy { it.id = 1L }
    columnRepository.save(ModelFactory
        .createModel(ColumnType.MONDAY))
    taskIdentification.setStrategy { it.id = 1L }

    val actual = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .body("""
            {
              "name": "Job",
              "description": "Go to job" 
            }
        """)
        .`when`()
        .post("/columns/{columnId}/tasks", 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString()

    val expected =
        """
        {
          id: 1,
          createdAt: (customized),
          name: 'Job',
          description: 'Go to job',
          index: 0,
          column: {
            id: 1,
            name: 'Monday tasks',
            index: 0
          }
        }  
        """
    JSONAssert
        .assertEquals(expected, actual, CustomComparator(JSONCompareMode.NON_EXTENSIBLE,
            Customization("createdAt") { act, _ -> act != null }
        ))
  }

  @Test
  fun get() {
    columnIdentification.setStrategy { it.id = 1L }
    val column: Column = columnRepository.save(ModelFactory
        .createModel(ColumnType.MONDAY))
    taskIdentification.setStrategy { it.id = 1L }
    taskRepository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also { it.column = column })

    val actual = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .`when`()
        .get("/columns/{columnId}/tasks/{id}", 1, 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString()

    val expected =
        """
        {
          id: 1,
          createdAt: (customized),
          name: 'Job',
          description: 'Go to job',
          index: 0,
          column: {
            id: 1,
            name: 'Monday tasks',
            index: 0
          }
        }  
        """
    JSONAssert
        .assertEquals(expected, actual, CustomComparator(JSONCompareMode.NON_EXTENSIBLE,
            Customization("createdAt") { act, _ -> act != null }
        ))
  }

  @Test
  fun update() {
    columnIdentification.setStrategy { it.id = 1L }
    val column: Column = columnRepository.save(ModelFactory
        .createModel(ColumnType.MONDAY))
    taskIdentification.setStrategy { it.id = 1L }
    taskRepository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also { it.column = column })

    val actual = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .body("""
            {
              "name": "Meeting",
              "description": "Meet John"
            }
        """)
        .`when`()
        .patch("/columns/{columnId}/tasks/{id}", 1, 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString()

    val expected =
        """
        {
          id: 1,
          createdAt: (customized),
          name: 'Meeting',
          description: 'Meet John',
          index: 0,
          column: {
            id: 1,
            name: 'Monday tasks',
            index: 0
          }
        }  
        """
    JSONAssert
        .assertEquals(expected, actual, CustomComparator(JSONCompareMode.NON_EXTENSIBLE,
            Customization("createdAt") { act, _ -> act != null }
        ))
  }

  @Test
  fun move() {
    columnIdentification.setStrategy { it.id = 1L }
    val monday: Column = columnRepository.save(ModelFactory
        .createModel(ColumnType.MONDAY))
    columnIdentification.setStrategy { it.id = 2L }
    val wednesday: Column = columnRepository.save(ModelFactory
        .createModel(ColumnType.WEDNESDAY))
    taskIdentification.setStrategy { it.id = 1L }
    val job: Task = taskRepository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also { it.column = monday })
    taskIdentification.setStrategy { it.id = 2L }
    val meeting: Task = taskRepository.save(ModelFactory
        .createModel(TaskType.MEETING)
        .also { it.column = wednesday })
    monday.tasks = arrayListOf(job)
    wednesday.tasks = arrayListOf(meeting)

    val actual = RestAssuredMockMvc
        .given()
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .body("""
            { 
              "newIndex": 0, 
              "newColumnId": 2 
            }  
        """)
        .`when`()
        .put("/columns/{columnId}/tasks/{id}", 1, 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)
        .extract()
        .asString()

    val expected =
        """
        {
          id: 1,
          createdAt: (customized),
          name: 'Job',
          description: 'Go to job',
          index: 0,
          column: {
            id: 2,
            name: 'Wednesday tasks',
            index: 1
          }
        }
       """.trimIndent()
    JSONAssert
        .assertEquals(expected, actual, CustomComparator(JSONCompareMode.NON_EXTENSIBLE,
            Customization("createdAt") { act, _ -> act != null }
        ))
  }

  @Test
  fun delete() {
    columnIdentification.setStrategy { it.id = 1L }
    val column: Column = columnRepository.save(ModelFactory
        .createModel(ColumnType.MONDAY))
    taskIdentification.setStrategy { it.id = 1L }
    taskRepository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also { it.column = column })

    RestAssuredMockMvc
        .delete("/columns/{columnId}/tasks/{id}", 1, 1)
        .then()
        .statusCode(HttpServletResponse.SC_OK)

    Assertions
        .assertThat(taskRepository.find(1L))
        .isNull()
  }


  @EnableWebMvc
  @EnableSpringDataWebSupport
  private class TestConfig

}
