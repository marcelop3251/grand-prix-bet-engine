package com.f1betting.infrastructure.persistence.entities

import com.f1betting.builders.EventBuilder
import com.f1betting.builders.DriverBuilder
import com.f1betting.builders.EventDriverBuilder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class EventDriverEntityTest {
    @Test
    fun `toDomain should convert EventDriverEntity to EventDriver domain object`() {
        val eventEntity = EventEntity.fromDomain(EventBuilder.build())
        val driverEntity = DriverEntity.fromDomain(DriverBuilder.build())
        val createdAt = LocalDateTime.now()
        val eventDriverEntity = EventDriverEntity(
            id = "ed1",
            event = eventEntity,
            driver = driverEntity,
            odds = BigDecimal(2),
            createdAt = createdAt
        )
        val result = eventDriverEntity.toDomain()
        assertEquals(eventDriverEntity.id, result.id)
        assertEquals(eventDriverEntity.event.id, result.event.id)
        assertEquals(eventDriverEntity.driver.id, result.driver.id)
        assertEquals(eventDriverEntity.odds, result.odds)
        assertEquals(eventDriverEntity.createdAt, result.createdAt)
    }

    @Test
    fun `fromDomain should convert EventDriver domain object to EventDriverEntity`() {
        val eventDriver = EventDriverBuilder.build()
        val result = EventDriverEntity.fromDomain(eventDriver)
        assertEquals(eventDriver.id, result.id)
        assertEquals(eventDriver.event.id, result.event.id)
        assertEquals(eventDriver.driver.id, result.driver.id)
        assertEquals(eventDriver.odds, result.odds)
        assertEquals(eventDriver.createdAt, result.createdAt)
    }

    @Test
    fun `toDomain and fromDomain should be reversible`() {
        val original = EventDriverBuilder.build()
        val entity = EventDriverEntity.fromDomain(original)
        val converted = entity.toDomain()
        assertEquals(original.id, converted.id)
        assertEquals(original.event.id, converted.event.id)
        assertEquals(original.driver.id, converted.driver.id)
        assertEquals(original.odds, converted.odds)
        assertEquals(original.createdAt, converted.createdAt)
    }
} 