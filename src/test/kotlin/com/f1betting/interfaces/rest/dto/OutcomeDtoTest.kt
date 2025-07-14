package com.f1betting.interfaces.rest.dto

import com.f1betting.builders.OutcomeBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OutcomeDtoTest {
    @Test
    fun `fromEntity should convert Outcome to OutcomeDto`() {
        val outcome = OutcomeBuilder.build()
        val result = OutcomeDto.fromEntity(outcome)
        assertEquals(outcome.id, result.id)
        assertEquals(outcome.createdAt, result.createdAt)
        assertEquals(outcome.event.id, result.event.id)
        assertEquals(outcome.winnerDriver.id, result.winnerDriver.id)
    }
} 