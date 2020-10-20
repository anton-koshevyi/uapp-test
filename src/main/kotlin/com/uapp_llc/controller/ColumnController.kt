package com.uapp_llc.controller

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
import com.uapp_llc.dto.column.ColumnDto
import com.uapp_llc.dto.column.ContentDto
import com.uapp_llc.dto.column.MoveDto
import com.uapp_llc.mapper.ColumnMapper
import com.uapp_llc.service.ColumnService

@RestController
class ColumnController @Autowired constructor(

    private val columnService: ColumnService

) {

  @GetMapping("/columns")
  fun getAll(@PageableDefault(sort = ["index"]) pageable: Pageable): Page<ColumnDto> =
      columnService.findAll(pageable)
          .map(ColumnMapper.INSTANCE::toDto)

  @PostMapping("/columns")
  fun create(@RequestBody dto: ContentDto): ColumnDto =
      // Possible NPE due absent validation
      columnService.create(dto.name!!)
          .let { ColumnMapper.INSTANCE.toDto(it) }

  @GetMapping("/columns/{id}")
  fun get(@PathVariable id: Long): ColumnDto =
      columnService.find(id)
          .let { ColumnMapper.INSTANCE.toDto(it) }

  @PatchMapping("/columns/{id}")
  fun update(@PathVariable id: Long,
             @RequestBody dto: ContentDto): ColumnDto =
      columnService.update(id, dto.name)
          .let { ColumnMapper.INSTANCE.toDto(it) }

  @PutMapping("/columns/{id}")
  fun changeIndex(@PathVariable id: Long,
                  @RequestBody dto: MoveDto): ColumnDto =
      // Possible NPE due absent validation
      columnService.changeIndex(id, dto.newIndex!!)
          .let { ColumnMapper.INSTANCE.toDto(it) }

  @DeleteMapping("/columns/{id}")
  fun delete(@PathVariable id: Long) =
      columnService.delete(id)

}
