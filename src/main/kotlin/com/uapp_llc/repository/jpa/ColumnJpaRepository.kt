package com.uapp_llc.repository.jpa

import org.springframework.data.jpa.repository.JpaRepository
import com.uapp_llc.model.Column

interface ColumnJpaRepository : JpaRepository<Column, Long>
