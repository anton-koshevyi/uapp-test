package com.uapp_llc.model

import java.time.ZonedDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "tasks")
class Task(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Column(name = "id")
    var id: Long? = null,

    @javax.persistence.Column(name = "created_at", nullable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now(),

    @javax.persistence.Column(name = "name")
    var name: String? = null,

    @javax.persistence.Column(name = "description")
    var description: String? = null,

    @javax.persistence.Column(name = "index", nullable = false)
    var index: Int

) {

  @ManyToOne(cascade = [CascadeType.MERGE])
  @JoinColumn(name = "column_id")
  lateinit var column: Column

}
