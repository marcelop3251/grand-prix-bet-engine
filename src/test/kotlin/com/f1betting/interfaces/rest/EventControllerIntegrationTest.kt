package com.f1betting.interfaces.rest

import com.f1betting.BaseIntegrationTest
import com.f1betting.interfaces.rest.dto.EventDriverDto
import com.f1betting.interfaces.rest.dto.EventDto
import org.junit.jupiter.api.Test
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EventControllerIntegrationTest : BaseIntegrationTest() {

    @Test
    fun `should integrate all components successfully for event listing`() {
        // Given - test data is already set up in BaseIntegrationTest
        
        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/events",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<EventDto>>() {}
        )

        // Then - Verify integration and data retrieval
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        val events = response.body!!
        assertEquals(1, events.size)
        
        // Verify DTO mapping worked correctly
        val event = events[0]
        assertEquals("Monaco Grand Prix", event.sessionName)
        assertEquals("Race", event.sessionType)
        assertEquals(2024, event.year)
        assertEquals("Monaco", event.countryName)
        assertEquals("MON", event.circuitShortName)
        assertEquals("Monte Carlo", event.location)
    }

    @Test
    fun `should integrate components for event details retrieval`() {
        // Given
        val testEvent = getTestEvent()
        
        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/events/${testEvent.id}",
            HttpMethod.GET,
            null,
            EventDto::class.java
        )

        // Then - Verify integration worked
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        val event = response.body!!
        assertEquals("Monaco Grand Prix", event.sessionName)
        assertEquals("Race", event.sessionType)
        assertEquals(2024, event.year)
    }

    @Test
    fun `should integrate components for event drivers retrieval`() {
        // Given
        val testEvent = getTestEvent()
        
        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/events/${testEvent.id}/drivers",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<EventDriverDto>>() {}
        )

        // Then - Verify integration and complex joins worked
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        val eventDrivers = response.body!!
        assertEquals(2, eventDrivers.size)
        
        // Verify complex DTO mapping with nested objects
        val driverNames = eventDrivers.map { it.driver.fullName }
        assertTrue(driverNames.contains("Lewis Hamilton"))
        assertTrue(driverNames.contains("Max Verstappen"))
        
        // Verify odds are properly mapped
        eventDrivers.forEach { eventDriver ->
            assertNotNull(eventDriver.odds)
            assertTrue(eventDriver.odds > java.math.BigDecimal.ZERO)
        }
    }

    @Test
    fun `should handle filtering and query parameters correctly`() {
        // Given - add another event with different year
        val eventEntity2024 = getTestEvent()
        val eventEntity2023 = eventEntity2024.copy(
            id = null,
            year = 2023,
            sessionName = "Brazilian Grand Prix",
            externalId = "456"
        )
        eventJpaRepository.save(eventEntity2023)

        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/events?year=2024",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<EventDto>>() {}
        )

        // Then - Verify filtering integration worked
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        val events = response.body!!
        assertEquals(1, events.size)
        assertEquals(2024, events[0].year)
        assertEquals("Monaco Grand Prix", events[0].sessionName)
    }

    @Test
    fun `should return proper HTTP status codes for different scenarios`() {
        // Test 404 for non-existent event
        val response404 = restTemplate.exchange(
            "${getBaseUrl()}/events/non-existent-id",
            HttpMethod.GET,
            null,
            String::class.java
        )
        assertEquals(HttpStatus.NOT_FOUND, response404.statusCode)

        // Test 404 for drivers of non-existent event
        val responseDrivers404 = restTemplate.exchange(
            "${getBaseUrl()}/events/non-existent-id/drivers",
            HttpMethod.GET,
            null,
            String::class.java
        )
        assertEquals(HttpStatus.NOT_FOUND, responseDrivers404.statusCode)
    }

    @Test
    fun `should serialize and deserialize complex DTOs correctly`() {
        // Given
        val testEvent = getTestEvent()
        
        // When
        val response = restTemplate.exchange(
            "${getBaseUrl()}/events/${testEvent.id}/drivers",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<EventDriverDto>>() {}
        )

        // Then - Verify complex DTO serialization
        assertEquals(HttpStatus.OK, response.statusCode)
        val eventDrivers = response.body!!
        
        eventDrivers.forEach { eventDriver ->
            // Verify all fields are properly serialized
            assertNotNull(eventDriver.id)
            assertNotNull(eventDriver.eventId)
            assertNotNull(eventDriver.driver)
            assertNotNull(eventDriver.odds)
            assertNotNull(eventDriver.createdAt)
            
            // Verify nested driver object
            assertNotNull(eventDriver.driver.id)
            assertNotNull(eventDriver.driver.fullName)
            assertNotNull(eventDriver.driver.driverNumber)
        }
    }
} 