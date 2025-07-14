package com.f1betting.infrastructure.persistence.entities

import com.f1betting.builders.DriverBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime

class DriverEntityTest {

    @Test
    fun `toDomain should convert DriverEntity to Driver domain object`() {
        // Given
        val driverEntity = DriverEntity(
            id = "driver1",
            externalId = "d1",
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
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        // When
        val result = driverEntity.toDomain()
        
        // Then
        assertEquals(driverEntity.id, result.id)
        assertEquals(driverEntity.externalId, result.externalId)
        assertEquals(driverEntity.driverNumber, result.driverNumber)
        assertEquals(driverEntity.firstName, result.firstName)
        assertEquals(driverEntity.lastName, result.lastName)
        assertEquals(driverEntity.fullName, result.fullName)
        assertEquals(driverEntity.broadcastName, result.broadcastName)
        assertEquals(driverEntity.nameAcronym, result.nameAcronym)
        assertEquals(driverEntity.countryCode, result.countryCode)
        assertEquals(driverEntity.teamName, result.teamName)
        assertEquals(driverEntity.teamColour, result.teamColour)
        assertEquals(driverEntity.headshotUrl, result.headshotUrl)
        assertEquals(driverEntity.createdAt, result.createdAt)
        assertEquals(driverEntity.updatedAt, result.updatedAt)
    }
    
    @Test
    fun `fromDomain should convert Driver domain object to DriverEntity`() {
        // Given
        val driver = DriverBuilder.build()
        
        // When
        val result = DriverEntity.fromDomain(driver)
        
        // Then
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
        assertEquals(driver.createdAt, result.createdAt)
        assertEquals(driver.updatedAt, result.updatedAt)
    }
    
    @Test
    fun `toDomain and fromDomain should be reversible`() {
        // Given
        val originalDriver = DriverBuilder.build()
        
        // When
        val driverEntity = DriverEntity.fromDomain(originalDriver)
        val convertedDriver = driverEntity.toDomain()
        
        // Then
        assertEquals(originalDriver.id, convertedDriver.id)
        assertEquals(originalDriver.externalId, convertedDriver.externalId)
        assertEquals(originalDriver.driverNumber, convertedDriver.driverNumber)
        assertEquals(originalDriver.firstName, convertedDriver.firstName)
        assertEquals(originalDriver.lastName, convertedDriver.lastName)
        assertEquals(originalDriver.fullName, convertedDriver.fullName)
        assertEquals(originalDriver.broadcastName, convertedDriver.broadcastName)
        assertEquals(originalDriver.nameAcronym, convertedDriver.nameAcronym)
        assertEquals(originalDriver.countryCode, convertedDriver.countryCode)
        assertEquals(originalDriver.teamName, convertedDriver.teamName)
        assertEquals(originalDriver.teamColour, convertedDriver.teamColour)
        assertEquals(originalDriver.headshotUrl, convertedDriver.headshotUrl)
        assertEquals(originalDriver.createdAt, convertedDriver.createdAt)
        assertEquals(originalDriver.updatedAt, convertedDriver.updatedAt)
    }
    
    @Test
    fun `should handle null optional fields`() {
        // Given
        val driverEntity = DriverEntity(
            id = "driver1",
            externalId = "d1",
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
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        // When
        val result = driverEntity.toDomain()
        
        // Then
        assertNull(result.broadcastName)
        assertNull(result.nameAcronym)
        assertNull(result.countryCode)
        assertNull(result.teamName)
        assertNull(result.teamColour)
        assertNull(result.headshotUrl)
    }
} 