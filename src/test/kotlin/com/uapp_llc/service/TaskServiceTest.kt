package com.uapp_llc.service

import com.uapp_llc.exception.IllegalActionException
import com.uapp_llc.exception.NotFoundException
import com.uapp_llc.model.Column
import com.uapp_llc.model.Task
import com.uapp_llc.test.comparator.ComparatorFactory
import com.uapp_llc.test.model.factory.ModelFactory
import com.uapp_llc.test.model.type.ColumnType
import com.uapp_llc.test.model.type.TaskType
import com.uapp_llc.test.stub.repository.TaskRepositoryStub
import com.uapp_llc.test.stub.repository.identification.IdentificationContext
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.data.domain.Pageable

class TaskServiceTest {

  private lateinit var identification: IdentificationContext<Task>
  private lateinit var repository: TaskRepositoryStub
  private lateinit var service: TaskService

  @BeforeEach
  fun setUp() {
    identification = IdentificationContext()
    repository = TaskRepositoryStub(identification)
    service = TaskServiceImpl(repository)
  }

  @Test
  fun create() {
    val column: Column = ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.id = 2L }
    identification.setStrategy { it.id = 1L }

    service.create(column, "Job", "Go to job")

    Assertions
        .assertThat(repository.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Task::class.java))
        .isEqualTo(ModelFactory
            .createModel(TaskType.JOB)
            .also {
              it.id = 1L
              it.column = ModelFactory
                  .createModel(ColumnType.MONDAY)
                  .also { c -> c.id = 2L }
            })
  }

  @Test
  fun update_whenNoEntityWithIdAndColumnId_expectException() {
    Assertions
        .assertThatThrownBy { service.update(1L, 2L, "Meeting", "Meet John") }
        .isExactlyInstanceOf(NotFoundException::class.java)
        .hasMessage("No task for id '1' and column id '2'")
  }

  @Test
  fun update() {
    val column: Column = ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.id = 2L }
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also {
          it.column = column
        })

    service.update(1L, 2L, "Meeting", "Meet John")

    Assertions
        .assertThat(repository.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Task::class.java))
        .isEqualTo(ModelFactory
            .createModel(TaskType.JOB)
            .also {
              it.id = 1L
              it.name = "Meeting"
              it.description = "Meet John"
              it.column = ModelFactory
                  .createModel(ColumnType.MONDAY)
                  .also { c -> c.id = 2L }
            })
  }

  @Test
  fun move_whenNotEntityWithIdAndColumnId_expectException() {
    Assertions
        .assertThatThrownBy { service.move(2L, 1L, null, 0) }
        .isExactlyInstanceOf(NotFoundException::class.java)
        .hasMessage("No task for id '2' and column id '1'")
  }

  @ParameterizedTest
  @ValueSource(ints = [-1, 2])
  fun move_whenIndexOutOfBounds_expectException(newIndex: Int) {
    val column: Column = ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.id = 1L }
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also {
          it.index = 0
          it.column = column
        })
    identification.setStrategy { it.id = 2L }
    repository.save(ModelFactory
        .createModel(TaskType.MEETING)
        .also {
          it.index = 1
          it.column = column
        })

    Assertions
        .assertThatThrownBy { service.move(2L, 1L, null, newIndex) }
        .isExactlyInstanceOf(IllegalActionException::class.java)
        .hasMessage("New task index out of bounds: $newIndex")
  }

  @Test
  fun move_whenNewIndexNotNull_andNewIndexEqualToActual_expectNoChanges() {
    val column: Column = ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.id = 1L }
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also {
          it.index = 0
          it.column = column
        })
    identification.setStrategy { it.id = 2L }
    repository.save(ModelFactory
        .createModel(TaskType.MEETING)
        .also {
          it.index = 1
          it.column = column
        })

    service.move(2L, 1L, null, 1)

    Assertions
        .assertThat(repository.findAll())
        .usingElementComparator(ComparatorFactory.getComparator(Task::class.java))
        .containsExactlyInAnyOrder(
            ModelFactory
                .createModel(TaskType.JOB)
                .also {
                  it.id = 1L
                  it.index = 0
                  it.column = ModelFactory
                      .createModel(ColumnType.MONDAY)
                      .also { c -> c.id = 1L }
                },
            ModelFactory
                .createModel(TaskType.MEETING)
                .also {
                  it.id = 2L
                  it.index = 1
                  it.column = ModelFactory
                      .createModel(ColumnType.MONDAY)
                      .also { c -> c.id = 1L }
                }
        )
  }

  @Test
  fun move_whenNewIndexNotNull_andNewIndexAfterActual() {
    val column: Column = ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.id = 1L }
    identification.setStrategy { it.id = 1L }
    val job: Task = repository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also {
          it.index = 0
          it.column = column
        })
    identification.setStrategy { it.id = 2L }
    val meeting: Task = repository.save(ModelFactory
        .createModel(TaskType.MEETING)
        .also {
          it.index = 1
          it.column = column
        })
    column.tasks = arrayListOf(job, meeting)

    service.move(1L, 1L, null, 1)

    Assertions
        .assertThat(repository.findAll())
        .usingElementComparator(ComparatorFactory.getComparator(Task::class.java))
        .containsExactlyInAnyOrder(
            ModelFactory
                .createModel(TaskType.JOB)
                .also {
                  it.id = 1L
                  it.index = 1
                  it.column = ModelFactory
                      .createModel(ColumnType.MONDAY)
                      .also { c -> c.id = 1L }
                },
            ModelFactory
                .createModel(TaskType.MEETING)
                .also {
                  it.id = 2L
                  it.index = 0
                  it.column = ModelFactory
                      .createModel(ColumnType.MONDAY)
                      .also { c -> c.id = 1L }
                }
        )
  }

  @Test
  fun move_whenNewIndexNotNull_andNewIndexBeforeActual() {
    val column: Column = ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.id = 1L }
    identification.setStrategy { it.id = 1L }
    val job: Task = repository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also {
          it.index = 0
          it.column = column
        })
    identification.setStrategy { it.id = 2L }
    val meeting: Task = repository.save(ModelFactory
        .createModel(TaskType.MEETING)
        .also {
          it.index = 1
          it.column = column
        })
    column.tasks = arrayListOf(job, meeting)

    service.move(2L, 1L, null, 0)

    Assertions
        .assertThat(repository.findAll())
        .usingElementComparator(ComparatorFactory.getComparator(Task::class.java))
        .containsExactlyInAnyOrder(
            ModelFactory
                .createModel(TaskType.JOB)
                .also {
                  it.id = 1L
                  it.index = 1
                  it.column = ModelFactory
                      .createModel(ColumnType.MONDAY)
                      .also { c -> c.id = 1L }
                },
            ModelFactory
                .createModel(TaskType.MEETING)
                .also {
                  it.id = 2L
                  it.index = 0
                  it.column = ModelFactory
                      .createModel(ColumnType.MONDAY)
                      .also { c -> c.id = 1L }
                }
        )
  }

  @Test
  fun move_whenNewColumnNotNull_expectMoveAtLastIndex() {
    val monday: Column = ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.id = 1L }
    val wednesday: Column = ModelFactory
        .createModel(ColumnType.WEDNESDAY)
        .also { it.id = 2L }
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also { it.column = monday })
    identification.setStrategy { it.id = 2L }
    val meeting: Task = repository.save(ModelFactory
        .createModel(TaskType.MEETING)
        .also { it.column = wednesday })
    wednesday.tasks = arrayListOf(meeting)

    Assertions
        .assertThat(service.move(1L, 1L, wednesday, null))
        .usingComparator(ComparatorFactory.getComparator(Task::class.java))
        .isEqualTo(ModelFactory
            .createModel(TaskType.JOB)
            .also {
              it.id = 1L
              it.index = 1
              it.column = ModelFactory
                  .createModel(ColumnType.WEDNESDAY)
                  .also { c -> c.id = 2L }
            })
  }

  @Test
  fun move() {
    val monday: Column = ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.id = 1L }
    val wednesday: Column = ModelFactory
        .createModel(ColumnType.WEDNESDAY)
        .also { it.id = 2L }
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also { it.column = monday })
    identification.setStrategy { it.id = 2L }
    val meeting: Task = repository.save(ModelFactory
        .createModel(TaskType.MEETING)
        .also { it.column = wednesday })
    wednesday.tasks = arrayListOf(meeting)

    Assertions
        .assertThat(service.move(1L, 1L, wednesday, 0))
        .usingComparator(ComparatorFactory.getComparator(Task::class.java))
        .isEqualTo(ModelFactory
            .createModel(TaskType.JOB)
            .also {
              it.id = 1L
              it.index = 0
              it.column = ModelFactory
                  .createModel(ColumnType.WEDNESDAY)
                  .also { c -> c.id = 2L }
            })
  }

  @Test
  fun find_whenNoEntityWithIdAndColumnId_expectException() {
    Assertions
        .assertThatThrownBy { service.find(1L, 1L) }
        .isExactlyInstanceOf(NotFoundException::class.java)
        .hasMessage("No task for id '1' and column id '1'")
  }

  @Test
  fun find() {
    val column: Column = ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.id = 1L }
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also { it.column = column })

    Assertions
        .assertThat(service.find(1L, 1L))
        .usingComparator(ComparatorFactory.getComparator(Task::class.java))
        .isEqualTo(ModelFactory
            .createModel(TaskType.JOB)
            .also {
              it.id = 1L
              it.column = ModelFactory
                  .createModel(ColumnType.MONDAY)
                  .also { c -> c.id = 1L }
            })
  }

  @Test
  fun findAll() {
    val column: Column = ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.id = 1L }
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also { it.column = column })
    identification.setStrategy { it.id = 2L }
    repository.save(ModelFactory
        .createModel(TaskType.MEETING)
        .also { it.column = column })

    Assertions
        .assertThat(service.findAll(1L, Pageable.unpaged()))
        .usingElementComparator(ComparatorFactory.getComparator(Task::class.java))
        .containsExactlyInAnyOrder(
            ModelFactory
                .createModel(TaskType.JOB)
                .also {
                  it.id = 1L
                  it.column = ModelFactory
                      .createModel(ColumnType.MONDAY)
                      .also { c -> c.id = 1L }
                },
            ModelFactory
                .createModel(TaskType.MEETING)
                .also {
                  it.id = 2L
                  it.column = ModelFactory
                      .createModel(ColumnType.MONDAY)
                      .also { c -> c.id = 1L }
                }
        )
  }

  @Test
  fun delete_whenNoEntityWithIdAndColumnId_expectException() {
    Assertions
        .assertThatThrownBy { service.delete(1L, 2L) }
        .isExactlyInstanceOf(NotFoundException::class.java)
        .hasMessage("No task for id '1' and column id '2'")
  }

  @Test
  fun delete_whenLastEntity_expectChangeIndexTo0() {
    val column: Column = ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.id = 1L }
    identification.setStrategy { it.id = 1L }
    val job: Task = repository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also {
          it.column = column
        })
    column.tasks = arrayListOf(job)

    service.delete(1L, 1L)

    Assertions
        .assertThat(repository.find(1L))
        .isNull()
  }

  @Test
  fun delete_expectChangeIndexToLast() {
    val column: Column = ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.id = 1L }
    identification.setStrategy { it.id = 1L }
    val job: Task = repository.save(ModelFactory
        .createModel(TaskType.JOB)
        .also {
          it.index = 0
          it.column = column
        })
    identification.setStrategy { it.id = 2L }
    val meeting: Task = repository.save(ModelFactory
        .createModel(TaskType.MEETING)
        .also {
          it.index = 1
          it.column = column
        })
    column.tasks = arrayListOf(job, meeting)

    service.delete(1L, 1L)

    Assertions
        .assertThat(repository.find(1L))
        .isNull()
    Assertions
        .assertThat(repository.find(2L))
        .usingComparator(ComparatorFactory.getComparator(Task::class.java))
        .isEqualTo(ModelFactory
            .createModel(TaskType.MEETING)
            .also {
              it.id = 2L
              it.index = 0
              it.column = ModelFactory
                  .createModel(ColumnType.MONDAY)
                  .also { c -> c.id = 1L }
            })
  }

}
