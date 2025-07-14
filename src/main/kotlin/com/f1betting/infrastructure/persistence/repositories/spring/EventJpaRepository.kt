package com.f1betting.infrastructure.persistence.repositories.spring

import com.f1betting.infrastructure.persistence.entities.EventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface EventJpaRepository : JpaRepository<EventEntity, String>, JpaSpecificationExecutor<EventEntity> {
    fun existsByExternalId(externalId: String): Boolean
} 