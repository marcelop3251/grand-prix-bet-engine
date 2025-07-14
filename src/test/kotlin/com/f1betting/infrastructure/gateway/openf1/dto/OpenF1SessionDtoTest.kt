package com.f1betting.infrastructure.gateway.openf1.dto

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class OpenF1SessionDtoTest {
    @Test
    fun `should create OpenF1SessionDto with all properties`() {
        val dateStart = ZonedDateTime.now()
        val dateEnd = ZonedDateTime.now().plusHours(2)
        val sessionDto = OpenF1SessionDto(
            sessionKey = 1,
            meetingKey = 1,
            sessionName = "GP Brasil",
            sessionType = "Race",
            dateStart = dateStart,
            dateEnd = dateEnd,
            year = 2024,
            countryName = "BR",
            circuitShortName = "BR",
            location = "São Paulo"
        )
        
        assertEquals(1, sessionDto.sessionKey)
        assertEquals(1, sessionDto.meetingKey)
        assertEquals("GP Brasil", sessionDto.sessionName)
        assertEquals("Race", sessionDto.sessionType)
        assertEquals(dateStart, sessionDto.dateStart)
        assertEquals(dateEnd, sessionDto.dateEnd)
        assertEquals(2024, sessionDto.year)
        assertEquals("BR", sessionDto.countryName)
        assertEquals("BR", sessionDto.circuitShortName)
        assertEquals("São Paulo", sessionDto.location)
    }

    @Test
    fun `should create OpenF1SessionDto with null dateEnd and location`() {
        val dateStart = ZonedDateTime.now()
        val sessionDto = OpenF1SessionDto(
            sessionKey = 1,
            meetingKey = 1,
            sessionName = "GP Brasil",
            sessionType = "Race",
            dateStart = dateStart,
            dateEnd = null,
            year = 2024,
            countryName = "BR",
            circuitShortName = "BR",
            location = null
        )
        
        assertNotNull(sessionDto)
        assertEquals(null, sessionDto.dateEnd)
        assertEquals(null, sessionDto.location)
    }
}

class OpenF1DriverDtoTest {
    @Test
    fun `should create OpenF1DriverDto with all properties`() {
        val driverDto = OpenF1DriverDto(
            driverNumber = 44,
            firstName = "Lewis",
            lastName = "Hamilton",
            fullName = "Lewis Hamilton",
            broadcastName = "HAM",
            nameAcronym = "HAM",
            countryCode = "GBR",
            teamName = "Mercedes",
            teamColour = "#00D2BE",
            headshotUrl = "https://example.com/ham.jpg",
            sessionKey = 1
        )
        
        assertEquals(44, driverDto.driverNumber)
        assertEquals("Lewis", driverDto.firstName)
        assertEquals("Hamilton", driverDto.lastName)
        assertEquals("Lewis Hamilton", driverDto.fullName)
        assertEquals("HAM", driverDto.broadcastName)
        assertEquals("HAM", driverDto.nameAcronym)
        assertEquals("GBR", driverDto.countryCode)
        assertEquals("Mercedes", driverDto.teamName)
        assertEquals("#00D2BE", driverDto.teamColour)
        assertEquals("https://example.com/ham.jpg", driverDto.headshotUrl)
        assertEquals(1, driverDto.sessionKey)
    }

    @Test
    fun `should create OpenF1DriverDto with null optional properties`() {
        val driverDto = OpenF1DriverDto(
            driverNumber = 44,
            firstName = "Lewis",
            lastName = "Hamilton",
            fullName = "Lewis Hamilton",
            broadcastName = null,
            nameAcronym = null,
            countryCode = null,
            teamName = null,
            teamColour = null,
            headshotUrl = null,
            sessionKey = 1
        )
        
        assertNotNull(driverDto)
        assertEquals(null, driverDto.broadcastName)
        assertEquals(null, driverDto.nameAcronym)
        assertEquals(null, driverDto.countryCode)
        assertEquals(null, driverDto.teamName)
        assertEquals(null, driverDto.teamColour)
        assertEquals(null, driverDto.headshotUrl)
    }
} 