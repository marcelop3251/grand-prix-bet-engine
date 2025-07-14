package com.f1betting.infrastructure.persistence.repositories

import com.f1betting.domain.entities.EventDriver
import com.f1betting.domain.repositories.EventDriverRepository
import com.f1betting.infrastructure.persistence.entities.EventDriverEntity
import com.f1betting.infrastructure.persistence.repositories.spring.EventDriverJpaRepository
import org.springframework.stereotype.Component

@Component
class EventDriverRepositoryImpl(
    private val eventDriverJpaRepository: EventDriverJpaRepository
) : EventDriverRepository {
    override fun findByEventId(eventId: String): List<EventDriver> =
        eventDriverJpaRepository.findByEventId(eventId).map { it.toDomain() }

    override fun findByEventIdAndDriverId(eventId: String, driverId: String): EventDriver? =
        eventDriverJpaRepository.findByEventIdAndDriverId(eventId, driverId)?.toDomain()

    override fun existsByEventIdAndDriverId(eventId: String, driverId: String): Boolean =
        eventDriverJpaRepository.existsByEventIdAndDriverId(eventId, driverId)

    override fun save(eventDriver: EventDriver): EventDriver =
        eventDriverJpaRepository.save(EventDriverEntity.fromDomain(eventDriver)).toDomain()
} 