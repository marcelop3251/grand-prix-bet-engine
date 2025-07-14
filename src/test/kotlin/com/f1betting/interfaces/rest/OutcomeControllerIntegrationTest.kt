package com.f1betting.interfaces.rest

import com.f1betting.BaseIntegrationTest
import com.f1betting.domain.entities.BetStatus
import com.f1betting.infrastructure.persistence.entities.BetEntity
import com.f1betting.infrastructure.persistence.entities.OutcomeEntity
import com.f1betting.interfaces.rest.dto.OutcomeDto
import com.f1betting.interfaces.rest.dto.request.SimulateOutcomeRequest
import java.math.BigDecimal
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OutcomeControllerIntegrationTest : BaseIntegrationTest() {

    @Test
    fun `should integrate all components successfully and process outcome`() {
        // Given
        val testUser = getTestUser()
        val testEvent = getTestEvent()
        val testEventDrivers = getTestEventDrivers()
        val winnerDriver = testEventDrivers.first()
        val loserDriver = testEventDrivers[1]
        
        // Create some bets to test outcome processing
        val winningBet = BetEntity(
            id = null,
            user = testUser,
            event = testEvent,
            driver = winnerDriver.driver,
            amount = BigDecimal("100.00"),
            odds = winnerDriver.odds,
            potentialWinnings = BigDecimal("100.00").multiply(winnerDriver.odds),
            status = BetStatus.PENDING
        )
        
        val losingBet = BetEntity(
            id = null,
            user = testUser,
            event = testEvent,
            driver = loserDriver.driver,
            amount = BigDecimal("50.00"),
            odds = loserDriver.odds,
            potentialWinnings = BigDecimal("50.00").multiply(loserDriver.odds),
            status = BetStatus.PENDING
        )
        
        betJpaRepository.saveAll(listOf(winningBet, losingBet))
        
        // Update user balance
        val updatedUser = testUser.copy(balance = testUser.balance.subtract(BigDecimal("150.00")))
        userJpaRepository.save(updatedUser)
        
        val simulateOutcomeRequest = SimulateOutcomeRequest(
            winnerDriverId = winnerDriver.driver.id!!
        )
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(simulateOutcomeRequest, headers)

        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/events/${testEvent.id}/outcome",
            HttpMethod.POST,
            requestEntity,
            OutcomeDto::class.java
        )

        // Then - Verify integration and complex business logic execution
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        val outcome = response.body!!
        
        // Verify outcome was created and persisted
        assertEquals(testEvent.id, outcome.event.id)
        assertEquals(winnerDriver.driver.id, outcome.winnerDriver.id)
        
        val savedOutcomes = outcomeJpaRepository.findAll()
        assertEquals(1, savedOutcomes.size)
        
        // Verify bets were processed (integration with bet processing)
        val processedBets = betJpaRepository.findAll()
        assertEquals(2, processedBets.size)
        
        // Verify user balance was updated with winnings (transaction worked)
        val finalUser = userJpaRepository.findById(testUser.id!!).orElse(null)
        assertNotNull(finalUser)
        
        val expectedBalance = BigDecimal("1000.00")
            .subtract(BigDecimal("150.00"))
            .add(BigDecimal("100.00").multiply(winnerDriver.odds))
        
        assertEquals(0, expectedBalance.compareTo(finalUser.balance))
    }

    @Test
    fun `should handle database transaction rollback on error`() {
        // Given - invalid request that should cause rollback
        val testEvent = getTestEvent()
        
        val simulateOutcomeRequest = SimulateOutcomeRequest(
            winnerDriverId = "non-existent-driver"
        )
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(simulateOutcomeRequest, headers)

        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/events/${testEvent.id}/outcome",
            HttpMethod.POST,
            requestEntity,
            String::class.java
        )

        // Then - Verify transaction rollback
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        
        // Verify no outcome was created (transaction rolled back)
        val savedOutcomes = outcomeJpaRepository.findAll()
        assertTrue(savedOutcomes.isEmpty())
    }

    @Test
    fun `should return proper HTTP status codes for different exceptions`() {
        // Given
        val testEvent = getTestEvent()
        val testDrivers = getTestDrivers()
        
        // Create a driver not associated with the event
        val unrelatedDriver = testDrivers.first().copy(
            id = null,
            externalId = "999",
            fullName = "Unrelated Driver"
        )
        val savedUnrelatedDriver = driverJpaRepository.save(unrelatedDriver)
        
        val simulateOutcomeRequest = SimulateOutcomeRequest(
            winnerDriverId = savedUnrelatedDriver.id!!
        )
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(simulateOutcomeRequest, headers)

        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/events/${testEvent.id}/outcome",
            HttpMethod.POST,
            requestEntity,
            String::class.java
        )

        // Then - Verify proper HTTP status mapping
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        
        // Verify no outcome was created
        val savedOutcomes = outcomeJpaRepository.findAll()
        assertTrue(savedOutcomes.isEmpty())
    }

    @Test
    fun `should serialize and deserialize DTOs correctly`() {
        // Given
        val testEvent = getTestEvent()
        val testEventDrivers = getTestEventDrivers()
        val winnerDriver = testEventDrivers.first()
        
        val simulateOutcomeRequest = SimulateOutcomeRequest(
            winnerDriverId = winnerDriver.driver.id!!
        )
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(simulateOutcomeRequest, headers)

        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/events/${testEvent.id}/outcome",
            HttpMethod.POST,
            requestEntity,
            OutcomeDto::class.java
        )

        // Then - Verify DTO mapping worked correctly
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        val outcome = response.body!!
        
        // Verify all fields are properly serialized
        assertNotNull(outcome.id)
        assertNotNull(outcome.event)
        assertNotNull(outcome.winnerDriver)
        assertNotNull(outcome.createdAt)
        
        // Verify nested objects are properly mapped
        assertEquals(testEvent.id, outcome.event.id)
        assertEquals(winnerDriver.driver.id, outcome.winnerDriver.id)
    }

    @Test
    fun `should handle duplicate outcome creation attempt`() {
        // Given
        val testEvent = getTestEvent()
        val testEventDrivers = getTestEventDrivers()
        val winnerDriver = testEventDrivers.first()
        
        // Create an existing outcome
        val existingOutcome = OutcomeEntity(
            id = null,
            event = testEvent,
            winnerDriver = winnerDriver.driver
        )
        outcomeJpaRepository.save(existingOutcome)
        
        val simulateOutcomeRequest = SimulateOutcomeRequest(
            winnerDriverId = winnerDriver.driver.id!!
        )
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(simulateOutcomeRequest, headers)

        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/events/${testEvent.id}/outcome",
            HttpMethod.POST,
            requestEntity,
            String::class.java
        )

        // Then - Verify conflict handling
        assertEquals(HttpStatus.CONFLICT, response.statusCode)
        
        // Verify only one outcome exists (the original one)
        val savedOutcomes = outcomeJpaRepository.findAll()
        assertEquals(1, savedOutcomes.size)
    }
}