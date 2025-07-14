package com.f1betting.infrastructure.persistence.entities

import com.f1betting.builders.EventBuilder
import com.f1betting.builders.DriverBuilder
import com.f1betting.builders.OutcomeBuilder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class OutcomeEntityTest {
    @Test
    fun `toDomain should convert OutcomeEntity to Outcome domain object`() {
        val eventEntity = EventEntity.fromDomain(EventBuilder.build())
        val driverEntity = DriverEntity.fromDomain(DriverBuilder.build())
        val createdAt = LocalDateTime.now()
        val outcomeEntity = OutcomeEntity(
            id = "outcome1",
            event = eventEntity,
            winnerDriver = driverEntity,
            createdAt = createdAt
        )
        val result = outcomeEntity.toDomain()
        assertEquals(outcomeEntity.id, result.id)
        assertEquals(outcomeEntity.event.id, result.event.id)
        assertEquals(outcomeEntity.winnerDriver.id, result.winnerDriver.id)
        assertEquals(outcomeEntity.createdAt, result.createdAt)
    }

    @Test
    fun `fromDomain should convert Outcome domain object to OutcomeEntity`() {
        val outcome = OutcomeBuilder.build()
        val result = OutcomeEntity.fromDomain(outcome)
        assertEquals(outcome.id, result.id)
        assertEquals(outcome.event.id, result.event.id)
        assertEquals(outcome.winnerDriver.id, result.winnerDriver.id)
        assertEquals(outcome.createdAt, result.createdAt)
    }

    @Test
    fun `toDomain and fromDomain should be reversible`() {
        val outcome = OutcomeBuilder.build()
        val entity = OutcomeEntity.fromDomain(outcome)
        val converted = entity.toDomain()
        assertEquals(outcome.id, converted.id)
        assertEquals(outcome.event.id, converted.event.id)
        assertEquals(outcome.winnerDriver.id, converted.winnerDriver.id)
        assertEquals(outcome.createdAt, converted.createdAt)
    }
} 