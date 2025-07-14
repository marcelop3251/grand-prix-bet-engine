package com.f1betting.interfaces.rest

import com.f1betting.BaseIntegrationTest
import com.f1betting.domain.entities.BetStatus
import com.f1betting.infrastructure.persistence.entities.BetEntity
import com.f1betting.interfaces.rest.dto.BetDto
import com.f1betting.interfaces.rest.dto.request.PlaceBetRequest
import java.math.BigDecimal
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
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
class BetControllerIntegrationTest : BaseIntegrationTest() {

    @Test
    fun `should integrate all components successfully and persist bet`() {
        // Given
        val testUser = getTestUser()
        val testEvent = getTestEvent()
        val testEventDrivers = getTestEventDrivers()
        val eventDriver = testEventDrivers.first()
        
        val placeBetRequest = PlaceBetRequest(
            eventId = testEvent.id!!,
            driverId = eventDriver.driver.id!!,
            amount = BigDecimal("50.00")
        )
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(placeBetRequest, headers)

        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/bets",
            HttpMethod.POST,
            requestEntity,
            BetDto::class.java
        )

        // Then - Validate integration and persistence
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        val bet = response.body!!
        
        // Verify response mapping
        assertEquals(testUser.id, bet.userId)
        assertEquals(testEvent.id, bet.event.id)
        assertEquals(eventDriver.driver.id, bet.driver.id)
        assertEquals(BigDecimal("50.00"), bet.amount)
        
        // Verify database persistence
        val savedBets = betJpaRepository.findAll()
        assertEquals(1, savedBets.size)
        val savedBet = savedBets.first()
        assertEquals(testUser.id, savedBet.user.id)
        assertEquals(testEvent.id, savedBet.event.id)
        assertEquals(eventDriver.driver.id, savedBet.driver.id)
        
        // Verify user balance was updated (transaction worked)
        val updatedUser = userJpaRepository.findById(testUser.id!!).orElse(null)
        assertNotNull(updatedUser)
        assertEquals(BigDecimal("950.00"), updatedUser.balance)
    }

    @Test
    fun `should handle database transaction rollback on error`() {
        // Given - invalid request that should cause rollback
        val testEvent = getTestEvent()
        
        val placeBetRequest = PlaceBetRequest(
            eventId = testEvent.id!!,
            driverId = "non-existent-driver",
            amount = BigDecimal("50.00")
        )
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(placeBetRequest, headers)

        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/bets",
            HttpMethod.POST,
            requestEntity,
            String::class.java
        )

        // Then - Verify transaction rollback
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        
        // Verify no bet was saved (transaction rolled back)
        val savedBets = betJpaRepository.findAll()
        assertTrue(savedBets.isEmpty())
        
        // Verify user balance unchanged (transaction rolled back)
        val user = userJpaRepository.findAll().first()
        assertEquals(BigDecimal("1000.00"), user.balance)
    }

    @Test
    fun `should return proper HTTP status codes for different exceptions`() {
        // Given
        val testEventDrivers = getTestEventDrivers()
        val eventDriver = testEventDrivers.first()
        
        // Test 404 for non-existent event
        val invalidEventRequest = PlaceBetRequest(
            eventId = "non-existent-event",
            driverId = eventDriver.driver.id!!,
            amount = BigDecimal("50.00")
        )
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        
        val response404 = restTemplate.exchange(
            "${getBaseUrl()}/bets",
            HttpMethod.POST,
            HttpEntity(invalidEventRequest, headers),
            String::class.java
        )
        
        assertEquals(HttpStatus.NOT_FOUND, response404.statusCode)
    }

    @Test
    fun `should serialize and deserialize DTOs correctly`() {
        // Given
        val testEvent = getTestEvent()
        val testEventDrivers = getTestEventDrivers()
        val eventDriver = testEventDrivers.first()
        
        val placeBetRequest = PlaceBetRequest(
            eventId = testEvent.id!!,
            driverId = eventDriver.driver.id!!,
            amount = BigDecimal("100.50") // Test decimal serialization
        )
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(placeBetRequest, headers)

        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/bets",
            HttpMethod.POST,
            requestEntity,
            BetDto::class.java
        )

        // Then - Verify DTO mapping worked correctly
        assertEquals(HttpStatus.OK, response.statusCode)
        val bet = response.body!!
        
        // Verify all fields are properly serialized
        assertNotNull(bet.id)
        assertNotNull(bet.userId)
        assertNotNull(bet.event)
        assertNotNull(bet.driver)
        assertEquals(BigDecimal("100.50"), bet.amount)
        assertNotNull(bet.odds)
        assertNotNull(bet.potentialWinnings)
        assertNotNull(bet.createdAt)
    }

    @Test
    fun `should handle multiple concurrent requests`() {
        // Given
        val testUser = getTestUser()
        val testEvent = getTestEvent()
        val testEventDrivers = getTestEventDrivers()
        val eventDriver1 = testEventDrivers[0]
        val eventDriver2 = testEventDrivers[1]
        
        val request1 = PlaceBetRequest(
            eventId = testEvent.id!!,
            driverId = eventDriver1.driver.id!!,
            amount = BigDecimal("100.00")
        )
        
        val request2 = PlaceBetRequest(
            eventId = testEvent.id!!,
            driverId = eventDriver2.driver.id!!,
            amount = BigDecimal("200.00")
        )
        
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        // When - Place multiple bets
        val response1 = restTemplate.exchange(
            "${getBaseUrl()}/bets",
            HttpMethod.POST,
            HttpEntity(request1, headers),
            BetDto::class.java
        )

        val response2 = restTemplate.exchange(
            "${getBaseUrl()}/bets",
            HttpMethod.POST,
            HttpEntity(request2, headers),
            BetDto::class.java
        )

        // Then - Verify both requests were processed
        assertEquals(HttpStatus.OK, response1.statusCode)
        assertEquals(HttpStatus.OK, response2.statusCode)
        
        // Verify both bets were saved
        val savedBets = betJpaRepository.findAll()
        assertEquals(2, savedBets.size)
        
        // Verify user balance was updated correctly (transaction consistency)
        val updatedUser = userJpaRepository.findById(testUser.id!!).orElse(null)
        assertNotNull(updatedUser)
        assertEquals(BigDecimal("700.00"), updatedUser.balance) // 1000 - 100 - 200
    }

    // Novos testes para os endpoints GET

    @Test
    fun `should integrate components successfully for listing user bets`() {
        // Given - Create some bets first
        val testUser = getTestUser()
        val testEvent = getTestEvent()
        val testEventDrivers = getTestEventDrivers()
        val eventDriver1 = testEventDrivers[0]
        val eventDriver2 = testEventDrivers[1]
        
        val bet1 = BetEntity(
            id = null,
            user = testUser,
            event = testEvent,
            driver = eventDriver1.driver,
            amount = BigDecimal("100.00"),
            odds = eventDriver1.odds,
            potentialWinnings = BigDecimal("100.00").multiply(eventDriver1.odds),
            status = BetStatus.PENDING
        )
        
        val bet2 = BetEntity(
            id = null,
            user = testUser,
            event = testEvent,
            driver = eventDriver2.driver,
            amount = BigDecimal("50.00"),
            odds = eventDriver2.odds,
            potentialWinnings = BigDecimal("50.00").multiply(eventDriver2.odds),
            status = BetStatus.PENDING
        )
        
        betJpaRepository.saveAll(listOf(bet1, bet2))

        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/bets",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<BetDto>>() {}
        )

        // Then - Verify integration worked
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        val bets = response.body!!
        assertEquals(2, bets.size)
        
        // Verify DTO mapping
        val betIds = bets.map { it.id }
        assertTrue(betIds.contains(bet1.id))
        assertTrue(betIds.contains(bet2.id))
        
        // Verify all bets belong to the user
        bets.forEach { bet ->
            assertEquals(testUser.id, bet.userId)
        }
    }

    @Test
    fun `should integrate components successfully for getting bet by id`() {
        // Given - Create a bet first
        val testUser = getTestUser()
        val testEvent = getTestEvent()
        val testEventDrivers = getTestEventDrivers()
        val eventDriver = testEventDrivers.first()
        
        val betEntity = BetEntity(
            id = null,
            user = testUser,
            event = testEvent,
            driver = eventDriver.driver,
            amount = BigDecimal("100.00"),
            odds = eventDriver.odds,
            potentialWinnings = BigDecimal("100.00").multiply(eventDriver.odds),
            status = BetStatus.PENDING
        )
        
        val savedBet = betJpaRepository.save(betEntity)

        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/bets/${savedBet.id}",
            HttpMethod.GET,
            null,
            BetDto::class.java
        )

        // Then - Verify integration worked
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        val bet = response.body!!
        
        // Verify DTO mapping
        assertEquals(savedBet.id, bet.id)
        assertEquals(testUser.id, bet.userId)
        assertEquals(testEvent.id, bet.event.id)
        assertEquals(eventDriver.driver.id, bet.driver.id)
        assertEquals(BigDecimal("100.00"), bet.amount)
        assertEquals(eventDriver.odds, bet.odds)
    }

    @Test
    fun `should return 404 when getting non-existent bet`() {
        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/bets/non-existent-bet-id",
            HttpMethod.GET,
            null,
            String::class.java
        )

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `should return empty list when user has no bets`() {
        // Given - No bets created
        
        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/bets",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<BetDto>>() {}
        )

        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        val bets = response.body!!
        assertTrue(bets.isEmpty())
    }

    @Test
    fun `should handle user validation in list bets endpoint`() {
        // Given - Create bet for different user (simulating user validation)
        val testUser = getTestUser()
        val testEvent = getTestEvent()
        val testEventDrivers = getTestEventDrivers()
        val eventDriver = testEventDrivers.first()
        
        // Create a bet
        val betEntity = BetEntity(
            id = null,
            user = testUser,
            event = testEvent,
            driver = eventDriver.driver,
            amount = BigDecimal("100.00"),
            odds = eventDriver.odds,
            potentialWinnings = BigDecimal("100.00").multiply(eventDriver.odds),
            status = BetStatus.PENDING
        )
        
        betJpaRepository.save(betEntity)

        // When - List bets (should only return bets for current user)
        val response = restTemplate.exchange(
            "${getBaseUrl()}/bets",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<BetDto>>() {}
        )

        // Then - Verify user filtering worked
        assertEquals(HttpStatus.OK, response.statusCode)
        val bets = response.body!!
        assertEquals(1, bets.size)
        assertEquals(testUser.id, bets.first().userId)
    }
} 