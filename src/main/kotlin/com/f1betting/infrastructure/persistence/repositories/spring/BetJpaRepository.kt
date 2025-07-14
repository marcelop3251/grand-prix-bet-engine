package com.f1betting.infrastructure.persistence.repositories.spring

import com.f1betting.domain.entities.BetStatus
import com.f1betting.infrastructure.persistence.entities.BetEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BetJpaRepository : JpaRepository<BetEntity, String> {
    fun findByUserId(userId: String): List<BetEntity>
    fun findByEventId(eventId: String): List<BetEntity>
} 