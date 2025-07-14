package com.f1betting.application.usecases

import com.f1betting.domain.entities.Event
import com.f1betting.domain.exceptions.EntityNotFoundException
import com.f1betting.domain.repositories.EventRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class GetEventUseCaseTest {
    private lateinit var eventRepository: EventRepository
    private lateinit var useCase: GetEventUseCase

    @BeforeEach
    fun setUp() {
        eventRepository = mockk(relaxed = true)
        useCase = GetEventUseCase(eventRepository)
    }

    @Test
    fun `should return event when found`() {
        // Given
        val eventId = "event1"
        val event = createEvent(eventId)
        
        every { eventRepository.findById(eventId) } returns event

        // When
        val result = useCase.execute(eventId)

        // Then
        assertEquals(event, result)
    }

    @Test
    fun `should throw EntityNotFoundException when event not found`() {
        // Given
        val eventId = "nonexistent"
        
        every { eventRepository.findById(eventId) } returns null

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
} 