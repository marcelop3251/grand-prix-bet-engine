package com.f1betting.interfaces.rest.dto

import com.f1betting.builders.EventDriverBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EventDriverDtoTest {
    @Test
    fun `fromEntity should convert EventDriver to EventDriverDto`() {
        val eventDriver = EventDriverBuilder.build()
        val result = EventDriverDto.fromEntity(eventDriver)
        assertEquals(eventDriver.id, result.id)
        assertEquals(eventDriver.event.id, result.eventId)
        assertEquals(eventDriver.odds, result.odds)
        assertEquals(eventDriver.createdAt, result.createdAt)
        assertEquals(eventDriver.driver.id, result.driver.id)
    }
} 