package com.f1betting.infrastructure.persistence.entities

import com.f1betting.builders.EventBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime

class EventEntityTest {

    @Test
    fun `toDomain should convert EventEntity to Event domain object`() {
        // Given
        val eventEntity = EventEntity(
            id = "event1",
            externalId = "ext1",
            meetingKey = 1,
            sessionKey = 1,
            year = 2024,
            countryName = "BR",
            circuitShortName = "BR",
            sessionName = "GP Brasil",
            sessionType = "Race",
            dateStart = LocalDateTime.now(),
            dateEnd = LocalDateTime.now().plusHours(2),
            location = "São Paulo",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        // When
        val result = eventEntity.toDomain()
        
        // Then
        assertEquals(eventEntity.id, result.id)
        assertEquals(eventEntity.externalId, result.externalId)
        assertEquals(eventEntity.meetingKey, result.meetingKey)
        assertEquals(eventEntity.sessionKey, result.sessionKey)
        assertEquals(eventEntity.year, result.year)
        assertEquals(eventEntity.countryName, result.countryName)
        assertEquals(eventEntity.circuitShortName, result.circuitShortName)
        assertEquals(eventEntity.sessionName, result.sessionName)
        assertEquals(eventEntity.sessionType, result.sessionType)
        assertEquals(eventEntity.dateStart, result.dateStart)
        assertEquals(eventEntity.dateEnd, result.dateEnd)
        assertEquals(eventEntity.location, result.location)
        assertEquals(eventEntity.createdAt, result.createdAt)
        assertEquals(eventEntity.updatedAt, result.updatedAt)
    }
    
    @Test
    fun `fromDomain should convert Event domain object to EventEntity`() {
        // Given
        val event = EventBuilder.build()
        
        // When
        val result = EventEntity.fromDomain(event)
        
        // Then
        assertEquals(event.id, result.id)
        assertEquals(event.externalId, result.externalId)
        assertEquals(event.meetingKey, result.meetingKey)
        assertEquals(event.sessionKey, result.sessionKey)
        assertEquals(event.year, result.year)
        assertEquals(event.countryName, result.countryName)
        assertEquals(event.circuitShortName, result.circuitShortName)
        assertEquals(event.sessionName, result.sessionName)
        assertEquals(event.sessionType, result.sessionType)
        assertEquals(event.dateStart, result.dateStart)
        assertEquals(event.dateEnd, result.dateEnd)
        assertEquals(event.location, result.location)
        assertEquals(event.createdAt, result.createdAt)
        assertEquals(event.updatedAt, result.updatedAt)
    }
    
    @Test
    fun `toDomain and fromDomain should be reversible`() {
        // Given
        val originalEvent = EventBuilder.build()
        
        // When
        val eventEntity = EventEntity.fromDomain(originalEvent)
        val convertedEvent = eventEntity.toDomain()
        
        // Then
        assertEquals(originalEvent.id, convertedEvent.id)
        assertEquals(originalEvent.externalId, convertedEvent.externalId)
        assertEquals(originalEvent.meetingKey, convertedEvent.meetingKey)
        assertEquals(originalEvent.sessionKey, convertedEvent.sessionKey)
        assertEquals(originalEvent.year, convertedEvent.year)
        assertEquals(originalEvent.countryName, convertedEvent.countryName)
        assertEquals(originalEvent.circuitShortName, convertedEvent.circuitShortName)
        assertEquals(originalEvent.sessionName, convertedEvent.sessionName)
        assertEquals(originalEvent.sessionType, convertedEvent.sessionType)
        assertEquals(originalEvent.dateStart, convertedEvent.dateStart)
        assertEquals(originalEvent.dateEnd, convertedEvent.dateEnd)
        assertEquals(originalEvent.location, convertedEvent.location)
        assertEquals(originalEvent.createdAt, convertedEvent.createdAt)
        assertEquals(originalEvent.updatedAt, convertedEvent.updatedAt)
    }
    
    @Test
    fun `should handle null dateEnd and location`() {
        // Given
        val eventEntity = EventEntity(
            id = "event1",
            externalId = "ext1",
            meetingKey = 1,
            sessionKey = 1,
            year = 2024,
            countryName = "BR",
            circuitShortName = "BR",
            sessionName = "GP Brasil",
            sessionType = "Race",
            dateStart = LocalDateTime.now(),
            dateEnd = null,
            location = null,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        // When
        val result = eventEntity.toDomain()
        
        // Then
        assertNull(result.dateEnd)
        assertNull(result.location)
    }
} 