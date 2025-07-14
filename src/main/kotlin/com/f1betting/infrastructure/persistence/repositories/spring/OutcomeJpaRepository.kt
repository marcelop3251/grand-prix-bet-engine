package com.f1betting.infrastructure.persistence.repositories.spring

import com.f1betting.infrastructure.persistence.entities.OutcomeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OutcomeJpaRepository : JpaRepository<OutcomeEntity, String> {
    fun findByEventId(eventId: String): OutcomeEntity?
    fun existsByEventId(eventId: String): Boolean
} 