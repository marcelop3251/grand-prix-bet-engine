package com.f1betting.domain.repositories

import com.f1betting.domain.entities.Driver

interface DriverRepository {
    fun findByExternalId(externalId: String): Driver?
    fun save(driver: Driver): Driver
    fun findById(id: String): Driver?
} 