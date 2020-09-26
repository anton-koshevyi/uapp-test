package com.uapp_llc.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "columns")
public class Column {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @javax.persistence.Column(name = "id")
  private Long id;

  @javax.persistence.Column(name = "name")
  private String name;

  @javax.persistence.Column(name = "index", nullable = false)
  private Integer index;

  @OneToMany(mappedBy = "column", cascade = CascadeType.REMOVE)
  @OrderBy("index")
  private List<Task> tasks = new ArrayList<>();

}
