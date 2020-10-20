package com.uapp_llc.test.stub.repository

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractRepositoryStub<ID, T> {

  protected val logger: Logger = LoggerFactory.getLogger(this::class.java)
  private val entities: MutableMap<ID?, T> = mutableMapOf()

  open fun save(id: ID?, entity: T): T {
    val isNew: Boolean = entities.containsKey(id)
    entities[id] = entity
    logger.debug("Entity with id '{}' {}: {} ", id, (if (isNew) "added" else "updated"), entity)
    return entity
  }

  open fun find(id: ID?): T? {
    val entity: T? = entities[id]
    logger.debug("Entity with id '{}' {}found", id, (if (entity == null) "not " else ""))
    return entity
  }

  open fun find(criteria: (T) -> Boolean): T? {
    return entities.values
        .find(criteria)
  }

  open fun exists(id: ID?): Boolean {
    val exists: Boolean = entities.containsKey(id)
    logger.debug("Entity with id '{}' {}exists", id, (if (exists) "" else "not "))
    return exists
  }

  open fun exists(criteria: (T) -> Boolean): Boolean {
    return entities.values
        .any(criteria)
  }

  open fun delete(id: ID?, entity: T) {
    val removed: Boolean = entities.remove(id, entity)
    logger.debug("Entity with id '{}' {}removed: {}", id, (if (removed) "" else "not "), entity)
  }

  open fun deleteAll() {
    val size: Int = entities.size
    entities.clear()
    logger.debug("Delete {} entities", size)
  }

  open fun size(): Int {
    val size: Int = entities.size
    logger.debug("Repository size: {}", size)
    return size
  }

  open fun findAll(): MutableList<T> {
    val all: MutableCollection<T> = entities.values
    logger.debug("{} entities found", all.size)
    return all.toMutableList()
  }

}
