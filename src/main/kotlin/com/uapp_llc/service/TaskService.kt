package com.uapp_llc.service

import com.uapp_llc.model.Column
import com.uapp_llc.model.Task
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TaskService {

  fun create(column: Column, name: String, description: String): Task

  fun update(id: Long, columnId: Long, name: String?, description: String?): Task

  fun move(id: Long, columnId: Long, newColumn: Column?, newIndex: Int?): Task

  fun find(id: Long, columnId: Long): Task

  fun findAll(columnId: Long, pageable: Pageable): Page<Task>

  fun delete(id: Long, columnId: Long)

}
