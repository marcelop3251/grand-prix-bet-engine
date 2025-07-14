package com.f1betting.infrastructure.persistence.repositories

import com.f1betting.builders.EventBuilder
import com.f1betting.infrastructure.persistence.entities.EventEntity
import com.f1betting.infrastructure.persistence.repositories.spring.EventJpaRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.jpa.domain.Specification
import java.util.*

class EventRepositoryImplTest {
    private lateinit var eventJpaRepository: EventJpaRepository
    private lateinit var eventRepository: EventRepositoryImpl

    @BeforeEach
    fun setUp() {
        eventJpaRepository = mockk(relaxed = true)
        eventRepository = EventRepositoryImpl(eventJpaRepository)
    }

    @Test
    fun `existsByExternalId should return boolean`() {
        every { eventJpaRepository.existsByExternalId("ext1") } returns true
        val result = eventRepository.existsByExternalId("ext1")
        assertTrue(result)
    }

    @Test
    fun `findAll with spec should return mapped events`() {
        val eventEntity = EventEntity.fromDomain(EventBuilder.build())
        val spec = mockk<Specification<EventEntity>>(relaxed = true)
        every { eventJpaRepository.findAll(spec) } returns listOf(eventEntity)
        @Suppress("UNCHECKED_CAST")
        val result = eventRepository.findAll(spec as Specification<com.f1betting.domain.entities.Event>)
        assertEquals(1, result.size)
        assertEquals(eventEntity.id, result[0].id)
    }

    @Test
    fun `findById should return mapped event if present`() {
        val eventEntity = EventEntity.fromDomain(EventBuilder.build())
        every { eventJpaRepository.findById("event1") } returns Optional.of(eventEntity)
        val result = eventRepository.findById("event1")
        assertNotNull(result)
        assertEquals(eventEntity.id, result?.id)
    }

    @Test
    fun `findById should return null if not present`() {
        every { eventJpaRepository.findById("event2") } returns Optional.empty()
        val result = eventRepository.findById("event2")
        assertNull(result)
    }

    @Test
    fun `save should persist and return mapped event`() {
        val event = EventBuilder.build()
        val eventEntity = EventEntity.fromDomain(event)
        every { eventJpaRepository.save(any()) } returns eventEntity
        val result = eventRepository.save(event)
        assertEquals(event.id, result.id)
    }

    @Test
    fun `existsById should return boolean`() {
        every { eventJpaRepository.existsById("event1") } returns true
        val result = eventRepository.existsById("event1")
        assertTrue(result)
    }
} 