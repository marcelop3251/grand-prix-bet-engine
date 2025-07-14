package com.f1betting.interfaces.rest.dto

import com.f1betting.builders.DriverBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DriverDtoTest {
    @Test
    fun `fromEntity should convert Driver to DriverDto`() {
        val driver = DriverBuilder.build()
        val result = DriverDto.fromEntity(driver)
        assertEquals(driver.id, result.id)
        assertEquals(driver.externalId, result.externalId)
        assertEquals(driver.driverNumber, result.driverNumber)
        assertEquals(driver.firstName, result.firstName)
        assertEquals(driver.lastName, result.lastName)
        assertEquals(driver.fullName, result.fullName)
        assertEquals(driver.broadcastName, result.broadcastName)
        assertEquals(driver.nameAcronym, result.nameAcronym)
        assertEquals(driver.countryCode, result.countryCode)
        assertEquals(driver.teamName, result.teamName)
        assertEquals(driver.teamColour, result.teamColour)
        assertEquals(driver.headshotUrl, result.headshotUrl)
        assertEquals(driver.getDisplayName(), result.displayName)
    }
} 