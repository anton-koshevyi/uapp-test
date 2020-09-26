package com.uapp_llc.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uapp_llc.model.Column;

public interface ColumnJpaRepository extends JpaRepository<Column, Long> {
}
