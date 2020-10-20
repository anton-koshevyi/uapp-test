package com.uapp_llc.controller

import com.uapp_llc.dto.column.ColumnDto
import com.uapp_llc.dto.column.ContentDto
import com.uapp_llc.dto.column.MoveDto
import com.uapp_llc.mapper.ColumnMapper
import com.uapp_llc.model.Column
import com.uapp_llc.service.ColumnService
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
class ColumnController @Autowired constructor(

    private val columnService: ColumnService

) {

  @GetMapping("/columns")
  fun getAll(@PageableDefault(sort = ["index"]) pageable: Pageable): Page<ColumnDto> {
    val columns: Page<Column> = columnService.findAll(pageable)
    return columns.map(ColumnMapper.INSTANCE::toDto)
  }

  @PostMapping("/columns")
  fun create(@RequestBody dto: ContentDto): ColumnDto {
    // Possible NPE due absent validation
    val column: Column = columnService.create(dto.name!!)
    return ColumnMapper.INSTANCE.toDto(column)
  }

  @GetMapping("/columns/{id}")
  fun get(@PathVariable id: Long): ColumnDto {
    val column: Column = columnService.find(id)
    return ColumnMapper.INSTANCE.toDto(column)
  }

  @PatchMapping("/columns/{id}")
  fun update(@PathVariable id: Long,
             @RequestBody dto: ContentDto): ColumnDto {
    val column: Column = columnService.update(id, dto.name)
    return ColumnMapper.INSTANCE.toDto(column)
  }

  @PutMapping("/columns/{id}")
  fun changeIndex(@PathVariable id: Long,
                  @RequestBody dto: MoveDto): ColumnDto {
    val column: Column = columnService.changeIndex(id, dto.newIndex!!)
    return ColumnMapper.INSTANCE.toDto(column)
  }

  @DeleteMapping("/columns/{id}")
  fun delete(@PathVariable id: Long) {
    columnService.delete(id)
  }

}
