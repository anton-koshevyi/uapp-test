package com.uapp_llc.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.data.domain.Pageable
import com.uapp_llc.exception.IllegalActionException
import com.uapp_llc.exception.NotFoundException
import com.uapp_llc.model.Column
import com.uapp_llc.test.comparator.ComparatorFactory
import com.uapp_llc.test.model.factory.ModelFactory
import com.uapp_llc.test.model.type.ColumnType
import com.uapp_llc.test.stub.repository.ColumnRepositoryStub
import com.uapp_llc.test.stub.repository.identification.IdentificationContext

class ColumnServiceTest {

  private lateinit var identification: IdentificationContext<Column>
  private lateinit var repository: ColumnRepositoryStub
  private lateinit var service: ColumnService

  @BeforeEach
  fun setUp() {
    identification = IdentificationContext()
    repository = ColumnRepositoryStub(identification)
    service = ColumnServiceImpl(repository)
  }

  @Test
  fun create() {
    identification.setStrategy { it.id = 1L }

    service.create("Monday tasks")

    Assertions
        .assertThat(repository.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Column::class.java))
        .isEqualTo(ModelFactory
            .createModel(ColumnType.MONDAY)
            .also { it.id = 1L })
  }

  @Test
  fun update_whenNoEntityWithIdAndProjectId_expectException() {
    Assertions
        .assertThatThrownBy { service.update(1L, "Tuesday tasks") }
        .isExactlyInstanceOf(NotFoundException::class.java)
        .hasMessage("No column for id '1'")
  }

  @Test
  fun update() {
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(ColumnType.MONDAY))

    service.update(1L, "Tuesday tasks")

    Assertions
        .assertThat(repository.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Column::class.java))
        .isEqualTo(ModelFactory
            .createModel(ColumnType.MONDAY)
            .also {
              it.id = 1L
              it.name = "Tuesday tasks"
            })
  }

  @Test
  fun changeIndex_whenNoEntityWithIdAndProjectId_expectException() {
    Assertions
        .assertThatThrownBy { service.changeIndex(1L, 0) }
        .isExactlyInstanceOf(NotFoundException::class.java)
        .hasMessage("No column for id '1'")
  }

  @ParameterizedTest
  @ValueSource(ints = [-1, 2])
  fun changeIndex_whenIndexOutOfBounds_expectException(newIndex: Int) {
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(ColumnType.MONDAY))
    identification.setStrategy { it.id = 2L }
    repository.save(ModelFactory
        .createModel(ColumnType.WEDNESDAY))

    Assertions
        .assertThatThrownBy { service.changeIndex(1L, newIndex) }
        .isExactlyInstanceOf(IllegalActionException::class.java)
        .hasMessage("New column index out of bounds: $newIndex")
  }

  @Test
  fun changeIndex_whenIndexEqualToActual_expectNoChanges() {
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.index = 0 })
    identification.setStrategy { it.id = 2L }
    repository.save(ModelFactory
        .createModel(ColumnType.WEDNESDAY)
        .also { it.index = 1 })

    service.changeIndex(1L, 0)

    Assertions
        .assertThat(repository.findAllByOrderByIndex())
        .usingElementComparator(ComparatorFactory.getComparator(Column::class.java))
        .containsExactlyInAnyOrder(
            ModelFactory
                .createModel(ColumnType.MONDAY)
                .also {
                  it.id = 1L
                  it.index = 0
                },
            ModelFactory
                .createModel(ColumnType.WEDNESDAY)
                .also {
                  it.id = 2L
                  it.index = 1
                }
        )
  }

  @Test
  fun changeIndex_whenIndexAfterActual() {
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.index = 0 })
    identification.setStrategy { it.id = 2L }
    repository.save(ModelFactory
        .createModel(ColumnType.WEDNESDAY)
        .also { it.index = 1 })

    service.changeIndex(2L, 0)

    Assertions
        .assertThat(repository.findAllByOrderByIndex())
        .usingElementComparator(ComparatorFactory.getComparator(Column::class.java))
        .containsExactlyInAnyOrder(
            ModelFactory
                .createModel(ColumnType.MONDAY)
                .also {
                  it.id = 1L
                  it.index = 1
                },
            ModelFactory
                .createModel(ColumnType.WEDNESDAY)
                .also {
                  it.id = 2L
                  it.index = 0
                }
        )
  }

  @Test
  fun changeIndex_whenIndexBeforeActual() {
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.index = 0 })
    identification.setStrategy { it.id = 2L }
    repository.save(ModelFactory
        .createModel(ColumnType.WEDNESDAY)
        .also { it.index = 1 })

    service.changeIndex(1L, 1)

    Assertions
        .assertThat(repository.findAllByOrderByIndex())
        .usingElementComparator(ComparatorFactory.getComparator(Column::class.java))
        .containsExactlyInAnyOrder(
            ModelFactory
                .createModel(ColumnType.MONDAY)
                .also {
                  it.id = 1L
                  it.index = 1
                },
            ModelFactory
                .createModel(ColumnType.WEDNESDAY)
                .also {
                  it.id = 2L
                  it.index = 0
                }
        )
  }

  @Test
  fun find_whenNoEntityWithIdAndProjectId_expectException() {
    Assertions
        .assertThatThrownBy { service.find(1L) }
        .isExactlyInstanceOf(NotFoundException::class.java)
        .hasMessage("No column for id '1'")
  }

  @Test
  fun find() {
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(ColumnType.MONDAY))

    Assertions
        .assertThat(service.find(1L))
        .usingComparator(ComparatorFactory.getComparator(Column::class.java))
        .isEqualTo(ModelFactory
            .createModel(ColumnType.MONDAY)
            .also { it.id = 1L })
  }

  @Test
  fun findAll() {
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(ColumnType.MONDAY))
    identification.setStrategy { it.id = 2L }
    repository.save(ModelFactory
        .createModel(ColumnType.WEDNESDAY))

    Assertions
        .assertThat(service.findAll(Pageable.unpaged()))
        .usingElementComparator(ComparatorFactory.getComparator(Column::class.java))
        .containsExactlyInAnyOrder(
            ModelFactory
                .createModel(ColumnType.MONDAY)
                .also { it.id = 1L }
            ,
            ModelFactory
                .createModel(ColumnType.WEDNESDAY)
                .also { it.id = 2L }
        )
  }

  @Test
  fun delete_whenNoEntityWithIdAndProjectId_expectException() {
    Assertions
        .assertThatThrownBy { service.delete(1L) }
        .isExactlyInstanceOf(NotFoundException::class.java)
        .hasMessage("No column for id '1'")
  }

  @Test
  fun delete_whenLastEntity_expectChangeIndexTo0() {
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(ColumnType.MONDAY))

    service.delete(1L)

    Assertions
        .assertThat(repository.find(1L))
        .isNull()
  }

  @Test
  fun delete_expectChangeIndexToLast() {
    identification.setStrategy { it.id = 1L }
    repository.save(ModelFactory
        .createModel(ColumnType.MONDAY)
        .also { it.index = 0 })
    identification.setStrategy { it.id = 2L }
    repository.save(ModelFactory
        .createModel(ColumnType.WEDNESDAY)
        .also { it.index = 1 })

    service.delete(1L)

    Assertions
        .assertThat(repository.find(1L))
        .isNull()
    Assertions
        .assertThat(repository.find(2L))
        .usingComparator(ComparatorFactory.getComparator(Column::class.java))
        .isEqualTo(ModelFactory
            .createModel(ColumnType.WEDNESDAY)
            .also {
              it.id = 2L
              it.index = 0
            })
  }

}
