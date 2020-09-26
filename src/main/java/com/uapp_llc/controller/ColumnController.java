package com.uapp_llc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uapp_llc.dto.column.ColumnDto;
import com.uapp_llc.dto.column.ContentDto;
import com.uapp_llc.dto.column.MoveDto;
import com.uapp_llc.mapper.ColumnMapper;
import com.uapp_llc.model.Column;
import com.uapp_llc.service.ColumnService;

@RestController
public class ColumnController {

  private final ColumnService columnService;

  @Autowired
  public ColumnController(ColumnService columnService) {
    this.columnService = columnService;
  }

  @GetMapping("/columns")
  public Page<ColumnDto> getAll(@RequestParam(required = false)
                                @PageableDefault(sort = "index") Pageable pageable) {
    Page<Column> columns = columnService.findAll(pageable);
    return columns.map(ColumnMapper.INSTANCE::toDto);
  }

  @PostMapping("/columns")
  public ColumnDto create(@RequestBody ContentDto dto) {
    Column column = columnService.create(dto.getName());
    return ColumnMapper.INSTANCE.toDto(column);
  }

  @GetMapping("/columns/{id}")
  public ColumnDto get(@PathVariable Long id) {
    Column column = columnService.find(id);
    return ColumnMapper.INSTANCE.toDto(column);
  }

  @PatchMapping("/columns/{id}")
  public ColumnDto update(@PathVariable Long id,
                          @RequestBody ContentDto dto) {
    Column column = columnService.update(id, dto.getName());
    return ColumnMapper.INSTANCE.toDto(column);
  }

  @PutMapping("/columns/{id}")
  public ColumnDto changeIndex(@PathVariable Long id,
                               @RequestBody MoveDto dto) {
    Column column = columnService.changeIndex(id, dto.getNewIndex());
    return ColumnMapper.INSTANCE.toDto(column);
  }

  @DeleteMapping("/columns/{id}")
  public void delete(@PathVariable Long id) {
    columnService.delete(id);
  }

}
