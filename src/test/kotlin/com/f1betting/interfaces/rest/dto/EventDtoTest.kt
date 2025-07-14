package com.f1betting.interfaces.rest.dto

import com.f1betting.builders.EventBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EventDtoTest {
    @Test
    fun `fromEntity should convert Event to EventDto`() {
        val event = EventBuilder.build()
        val result = EventDto.fromEntity(event)
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
    }
} 