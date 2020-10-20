package com.uapp_llc.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories("com.uapp_llc.repository.jpa")
@EntityScan("com.uapp_llc.model")
class DatabaseConfig
