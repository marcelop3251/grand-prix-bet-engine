package com.f1betting.application.usecases

import com.f1betting.domain.entities.Driver
import com.f1betting.domain.entities.Event
import com.f1betting.domain.entities.EventDriver
import com.f1betting.domain.exceptions.EntityNotFoundException
import com.f1betting.domain.repositories.EventDriverRepository
import com.f1betting.domain.repositories.EventRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class GetEventDriversUseCaseTest {
    private lateinit var eventRepository: EventRepository
    private lateinit var eventDriverRepository: EventDriverRepository
    private lateinit var useCase: GetEventDriversUseCase

    @BeforeEach
    fun setUp() {
        eventRepository = mockk(relaxed = true)
        eventDriverRepository = mockk(relaxed = true)
        useCase = GetEventDriversUseCase(eventRepository, eventDriverRepository)
    }

    @Test
    fun `should return event drivers when event exists`() {
        // Given
        val eventId = "event1"
        val event = createEvent(eventId)
        val driver1 = createDriver("driver1")
        val driver2 = createDriver("driver2")
        val eventDrivers = listOf(
            createEventDriver(event, driver1),
            createEventDriver(event, driver2)
        )
        
        every { eventRepository.existsById(eventId) } returns true
        every { eventDriverRepository.findByEventId(eventId) } returns eventDrivers

        // When
        val result = useCase.execute(eventId)

        // Then
        assertEquals(eventDrivers, result)
        assertEquals(2, result.size)
    }

    @Test
    fun `should return empty list when event exists but has no drivers`() {
        // Given
        val eventId = "event1"
        
        every { eventRepository.existsById(eventId) } returns true
        every { eventDriverRepository.findByEventId(eventId) } returns emptyList()

        // When
        val result = useCase.execute(eventId)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should throw EntityNotFoundException when event not found`() {
        // Given
        val eventId = "nonexistent"
        
        every { eventRepository.existsById(eventId) } returns false

        // When & Then
        assertThrows(EntityNotFoundException::class.java) {
            useCase.execute(eventId)
        }
    }

    private fun createEvent(id: String = "event1"): Event =
        Event(
            id = id,
            externalId = "ext1",
            meetingKey = 1,
            sessionKey = 1,
            year = 2024,
            countryName = "Monaco",
            circuitShortName = "MON",
            sessionName = "Monaco Grand Prix",
            sessionType = "Race",
            dateStart = LocalDateTime.now(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

    private fun createDriver(id: String = "driver1"): Driver =
        Driver(
            id = id,
            externalId = "d1",
            driverNumber = 1,
            firstName = "Max",
            lastName = "Verstappen",
            fullName = "Max Verstappen",
            broadcastName = null,
            nameAcronym = null,
            countryCode = null,
            teamName = null,
            teamColour = null,
            headshotUrl = null,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

    private fun createEventDriver(event: Event, driver: Driver): EventDriver =
        EventDriver(
            id = "ed1",
            event = event,
            driver = driver,
            odds = BigDecimal("2.5"),
            createdAt = LocalDateTime.now()
        )
} 