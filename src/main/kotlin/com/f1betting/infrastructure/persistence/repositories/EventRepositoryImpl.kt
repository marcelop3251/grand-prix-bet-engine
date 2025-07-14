package com.f1betting.infrastructure.persistence.repositories

import com.f1betting.domain.entities.Event
import com.f1betting.domain.repositories.EventRepository
import com.f1betting.infrastructure.persistence.entities.EventEntity
import com.f1betting.infrastructure.persistence.repositories.spring.EventJpaRepository
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component

@Component
class EventRepositoryImpl(
    private val eventJpaRepository: EventJpaRepository
) : EventRepository {

    override fun existsByExternalId(externalId: String): Boolean =
        eventJpaRepository.existsByExternalId(externalId)

    @Suppress("UNCHECKED_CAST")
    override fun findAll(spec: Specification<Event>): List<Event> =
        eventJpaRepository.findAll(spec as Specification<EventEntity>).map { it.toDomain() }

    override fun findById(id: String): Event? =
        eventJpaRepository.findById(id).orElse(null)?.toDomain()

    override fun save(event: Event): Event =
        eventJpaRepository.save(EventEntity.fromDomain(event)).toDomain()

    override fun existsById(id: String): Boolean =
        eventJpaRepository.existsById(id)
} 