package com.uapp_llc.controller

import com.uapp_llc.dto.task.ContentDto
import com.uapp_llc.dto.task.MoveDto
import com.uapp_llc.dto.task.TaskDto
import com.uapp_llc.mapper.TaskMapper
import com.uapp_llc.model.Column
import com.uapp_llc.model.Task
import com.uapp_llc.service.ColumnService
import com.uapp_llc.service.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TaskController @Autowired constructor(

    private val columnService: ColumnService,
    private val taskService: TaskService

) {

  @GetMapping("/columns/{columnId}/tasks")
  fun getAll(@PathVariable columnId: Long,
             @PageableDefault(sort = ["index"]) pageable: Pageable): Page<TaskDto> {
    val tasks: Page<Task> = taskService.findAll(columnId, pageable)
    return tasks.map(TaskMapper.INSTANCE::toDto)
  }

  @PostMapping("/columns/{columnId}/tasks")
  fun create(@PathVariable columnId: Long,
             @RequestBody dto: ContentDto): TaskDto {
    val column: Column = columnService.find(columnId)
    // Possible NPE due absent validation
    val task: Task = taskService.create(
        column,
        dto.name!!,
        dto.description!!
    )
    return TaskMapper.INSTANCE.toDto(task)
  }

  @GetMapping("/columns/{columnId}/tasks/{id}")
  fun get(@PathVariable columnId: Long,
          @PathVariable id: Long): TaskDto {
    val task: Task = taskService.find(id, columnId)
    return TaskMapper.INSTANCE.toDto(task)
  }

  @PatchMapping("/columns/{columnId}/tasks/{id}")
  fun update(@PathVariable columnId: Long,
             @PathVariable id: Long,
             @RequestBody dto: ContentDto): TaskDto {
    val task: Task = taskService.update(
        id,
        columnId,
        dto.name,
        dto.description
    )
    return TaskMapper.INSTANCE.toDto(task)
  }

  @PutMapping("/columns/{columnId}/tasks/{id}")
  fun move(@PathVariable columnId: Long,
           @PathVariable id: Long,
           @RequestBody dto: MoveDto): TaskDto {
    val newColumnId: Long? = dto.newColumnId
    val newColumn: Column? =
        if (newColumnId != null && newColumnId != columnId)
          columnService.find(newColumnId)
        else null
    val task: Task = taskService.move(
        id,
        columnId,
        newColumn,
        dto.newIndex
    )
    return TaskMapper.INSTANCE.toDto(task)
  }

  @DeleteMapping("/columns/{columnId}/tasks/{id}")
  fun delete(@PathVariable columnId: Long,
             @PathVariable id: Long) {
    taskService.delete(id, columnId)
  }

}
