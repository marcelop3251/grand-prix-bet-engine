package com.f1betting.domain.repositories

import com.f1betting.domain.entities.EventDriver

interface EventDriverRepository {
    fun findByEventId(eventId: String): List<EventDriver>
    fun findByEventIdAndDriverId(eventId: String, driverId: String): EventDriver?
    fun existsByEventIdAndDriverId(eventId: String, driverId: String): Boolean
    fun save(eventDriver: EventDriver): EventDriver
} 