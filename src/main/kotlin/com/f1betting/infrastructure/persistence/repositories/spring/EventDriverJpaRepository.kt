package com.f1betting.infrastructure.persistence.repositories.spring

import com.f1betting.infrastructure.persistence.entities.EventDriverEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventDriverJpaRepository : JpaRepository<EventDriverEntity, String> {
    fun findByEventId(eventId: String): List<EventDriverEntity>
    fun findByEventIdAndDriverId(eventId: String, driverId: String): EventDriverEntity?
    fun existsByEventIdAndDriverId(eventId: String, driverId: String): Boolean
} 