package com.f1betting.infrastructure.persistence.entities

import com.f1betting.builders.BetBuilder
import com.f1betting.builders.UserBuilder
import com.f1betting.builders.EventBuilder
import com.f1betting.builders.DriverBuilder
import com.f1betting.domain.entities.BetStatus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.math.BigDecimal
import java.time.LocalDateTime

class BetEntityTest {

    @Test
    fun `toDomain should convert BetEntity to Bet domain object`() {
        // Given
        val userEntity = UserEntity(
            id = "user1",
            username = "testuser",
            email = "test@test.com",
            balance = BigDecimal(100),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
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
        
        val driverEntity = DriverEntity(
            id = "driver1",
            externalId = "d1",
            driverNumber = 1,
            firstName = "A",
            lastName = "B",
            fullName = "A B",
            broadcastName = null,
            nameAcronym = null,
            countryCode = null,
            teamName = null,
            teamColour = null,
            headshotUrl = null,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        val betEntity = BetEntity(
            id = "bet1",
            user = userEntity,
            event = eventEntity,
            driver = driverEntity,
            amount = BigDecimal(10),
            odds = BigDecimal(2),
            potentialWinnings = BigDecimal(20),
            status = BetStatus.PENDING,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        // When
        val result = betEntity.toDomain()
        
        // Then
        assertEquals(betEntity.id, result.id)
        assertEquals(betEntity.amount, result.amount)
        assertEquals(betEntity.odds, result.odds)
        assertEquals(betEntity.potentialWinnings, result.potentialWinnings)
        assertEquals(betEntity.status, result.status)
        assertEquals(betEntity.createdAt, result.createdAt)
        assertEquals(betEntity.updatedAt, result.updatedAt)
        assertEquals(userEntity.id, result.user.id)
        assertEquals(eventEntity.id, result.event.id)
        assertEquals(driverEntity.id, result.driver.id)
    }
    
    @Test
    fun `fromDomain should convert Bet domain object to BetEntity`() {
        // Given
        val bet = BetBuilder.build()
        
        // When
        val result = BetEntity.fromDomain(bet)
        
        // Then
        assertEquals(bet.id, result.id)
        assertEquals(bet.amount, result.amount)
        assertEquals(bet.odds, result.odds)
        assertEquals(bet.potentialWinnings, result.potentialWinnings)
        assertEquals(bet.status, result.status)
        assertEquals(bet.createdAt, result.createdAt)
        assertEquals(bet.updatedAt, result.updatedAt)
        assertEquals(bet.user.id, result.user.id)
        assertEquals(bet.event.id, result.event.id)
        assertEquals(bet.driver.id, result.driver.id)
    }
    
    @Test
    fun `toDomain and fromDomain should be reversible`() {
        // Given
        val originalBet = BetBuilder.build()
        
        // When
        val betEntity = BetEntity.fromDomain(originalBet)
        val convertedBet = betEntity.toDomain()
        
        // Then
        assertEquals(originalBet.id, convertedBet.id)
        assertEquals(originalBet.amount, convertedBet.amount)
        assertEquals(originalBet.odds, convertedBet.odds)
        assertEquals(originalBet.potentialWinnings, convertedBet.potentialWinnings)
        assertEquals(originalBet.status, convertedBet.status)
        assertEquals(originalBet.createdAt, convertedBet.createdAt)
        assertEquals(originalBet.updatedAt, convertedBet.updatedAt)
    }
} 