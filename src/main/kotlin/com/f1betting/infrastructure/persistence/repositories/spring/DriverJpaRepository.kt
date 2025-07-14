package com.f1betting.infrastructure.persistence.repositories.spring

import com.f1betting.infrastructure.persistence.entities.DriverEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DriverJpaRepository : JpaRepository<DriverEntity, String> {
    fun findByExternalId(externalId: String): DriverEntity?
} 