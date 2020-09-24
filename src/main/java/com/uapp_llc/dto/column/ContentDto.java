package com.uapp_llc.dto.column;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import com.uapp_llc.constraint.ColumnName;

@Getter
@Setter
public class ContentDto {

  @NotNull(groups = CreateColumn.class)
  @ColumnName(groups = {CreateColumn.class, UpdateColumn.class})
  private String name;


  public interface CreateColumn {
  }

  public interface UpdateColumn {
  }

}
