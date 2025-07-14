package com.f1betting.application.usecases

import com.f1betting.builders.EventBuilder
import com.f1betting.application.usecases.commands.ListEventsCommand
import com.f1betting.domain.entities.Event
import com.f1betting.domain.repositories.EventRepository
import com.f1betting.domain.specifications.EventFilterStrategyFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.jpa.domain.Specification

class ListEventsUseCaseTest {
    private lateinit var eventRepository: EventRepository
    private lateinit var filterStrategyFactory: EventFilterStrategyFactory
    private lateinit var listEventsUseCase: ListEventsUseCase

    @BeforeEach
    fun setUp() {
        eventRepository = mockk()
        filterStrategyFactory = mockk()
        listEventsUseCase = ListEventsUseCase(eventRepository, filterStrategyFactory)
    }

    @Test
    fun `should return all events when no filters provided`() {
        val command = ListEventsCommand()
        val events = listOf(
            EventBuilder.build(),
            EventBuilder.build { id = "event2" }
        )
        val specification = mockk<Specification<Event>>() 
        every { filterStrategyFactory.buildSpecification(command) } returns specification
        every { eventRepository.findAll(specification) } returns events
        val result = listEventsUseCase.execute(command)
        assertEquals(events, result)
        verify { filterStrategyFactory.buildSpecification(command) }
        verify { eventRepository.findAll(specification) }
    }

    @Test
    fun `should filter by year when only year provided`() {
        val command = ListEventsCommand(year = 2023)
        val events = listOf(
            EventBuilder.build { year = 2023 }
        )
        val specification = mockk<Specification<Event>>() 
        every { filterStrategyFactory.buildSpecification(command) } returns specification
        every { eventRepository.findAll(specification) } returns events
        val result = listEventsUseCase.execute(command)
        assertEquals(events, result)
        verify { filterStrategyFactory.buildSpecification(command) }
        verify { eventRepository.findAll(specification) }
    }

    @Test
    fun `should filter by country when only country provided`() {
        val command = ListEventsCommand(country = "Monaco")
        val events = listOf(
            EventBuilder.build { countryName = "Monaco" }
        )
        val specification = mockk<Specification<Event>>() 
        every { filterStrategyFactory.buildSpecification(command) } returns specification
        every { eventRepository.findAll(specification) } returns events
        val result = listEventsUseCase.execute(command)
        assertEquals(events, result)
        verify { filterStrategyFactory.buildSpecification(command) }
        verify { eventRepository.findAll(specification) }
    }

    @Test
    fun `should filter by multiple criteria when provided`() {
        val command = ListEventsCommand(year = 2023, country = "Monaco", sessionType = "Race")
        val events = listOf(
            EventBuilder.build { 
                year = 2023
                countryName = "Monaco"
                sessionType = "Race"
            }
        )
        val specification = mockk<Specification<Event>>() 
        every { filterStrategyFactory.buildSpecification(command) } returns specification
        every { eventRepository.findAll(specification) } returns events
        val result = listEventsUseCase.execute(command)
        assertEquals(events, result)
        verify { filterStrategyFactory.buildSpecification(command) }
        verify { eventRepository.findAll(specification) }
    }
} 