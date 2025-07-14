package com.f1betting.infrastructure.persistence.repositories

import com.f1betting.builders.EventDriverBuilder
import com.f1betting.infrastructure.persistence.entities.EventDriverEntity
import com.f1betting.infrastructure.persistence.repositories.spring.EventDriverJpaRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EventDriverRepositoryImplTest {
    private lateinit var eventDriverJpaRepository: EventDriverJpaRepository
    private lateinit var eventDriverRepository: EventDriverRepositoryImpl

    @BeforeEach
    fun setUp() {
        eventDriverJpaRepository = mockk(relaxed = true)
        eventDriverRepository = EventDriverRepositoryImpl(eventDriverJpaRepository)
    }

    @Test
    fun `findByEventId should return mapped event drivers`() {
        val eventDriverEntity = EventDriverEntity.fromDomain(EventDriverBuilder.build())
        every { eventDriverJpaRepository.findByEventId("event1") } returns listOf(eventDriverEntity)
        val result = eventDriverRepository.findByEventId("event1")
        assertEquals(1, result.size)
        assertEquals(eventDriverEntity.id, result[0].id)
    }

    @Test
    fun `findByEventIdAndDriverId should return mapped event driver`() {
        val eventDriverEntity = EventDriverEntity.fromDomain(EventDriverBuilder.build())
        every { eventDriverJpaRepository.findByEventIdAndDriverId("event1", "driver1") } returns eventDriverEntity
        val result = eventDriverRepository.findByEventIdAndDriverId("event1", "driver1")
        assertNotNull(result)
        assertEquals(eventDriverEntity.id, result?.id)
    }

    @Test
    fun `existsByEventIdAndDriverId should return boolean`() {
        every { eventDriverJpaRepository.existsByEventIdAndDriverId("event1", "driver1") } returns true
        val result = eventDriverRepository.existsByEventIdAndDriverId("event1", "driver1")
        assertTrue(result)
    }

    @Test
    fun `save should persist and return mapped event driver`() {
        val eventDriver = EventDriverBuilder.build()
        val eventDriverEntity = EventDriverEntity.fromDomain(eventDriver)
        every { eventDriverJpaRepository.save(any()) } returns eventDriverEntity
        val result = eventDriverRepository.save(eventDriver)
        assertEquals(eventDriver.id, result.id)
    }
} 