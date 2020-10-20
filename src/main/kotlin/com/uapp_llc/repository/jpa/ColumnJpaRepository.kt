package com.uapp_llc.repository.jpa

import com.uapp_llc.model.Column
import org.springframework.data.jpa.repository.JpaRepository

interface ColumnJpaRepository : JpaRepository<Column, Long>
