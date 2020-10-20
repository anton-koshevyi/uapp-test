package com.uapp_llc.model

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.Table

@Entity
@Table(name = "columns")
class Column(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Column(name = "id")
    var id: Long? = null,

    @javax.persistence.Column(name = "name")
    var name: String? = null,

    @javax.persistence.Column(name = "index", nullable = false)
    var index: Int

) {

  @OneToMany(mappedBy = "column", cascade = [CascadeType.REMOVE])
  @OrderBy("index")
  var tasks: MutableList<Task> = mutableListOf()

}
