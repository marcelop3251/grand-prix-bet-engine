package com.f1betting.domain.repositories

import com.f1betting.domain.entities.Event
import org.springframework.data.jpa.domain.Specification

interface EventRepository {
    fun existsByExternalId(externalId: String): Boolean
    fun findAll(spec: Specification<Event>): List<Event>
    fun findById(id: String): Event?
    fun save(event: Event): Event
    fun existsById(id: String): Boolean
} 