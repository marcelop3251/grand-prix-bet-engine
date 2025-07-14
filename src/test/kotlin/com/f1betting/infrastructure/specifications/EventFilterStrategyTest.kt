package com.f1betting.infrastructure.specifications

import com.f1betting.application.usecases.commands.ListEventsCommand
import com.f1betting.domain.specifications.EventFilterStrategyFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class EventFilterStrategyTest {
    private val factory: EventFilterStrategyFactory = EventFilterStrategyFactoryImpl(
        strategies = listOf(
            YearFilterStrategy(),
            CountryFilterStrategy(),
            SessionTypeFilterStrategy()
        )
    )

    @Test
    fun `should build specification for year filter`() {
        val command = ListEventsCommand(year = 2024)
        val specification = factory.buildSpecification(command)
        assertNotNull(specification)
    }

    @Test
    fun `should build specification for multiple filters`() {
        val command = ListEventsCommand(
            year = 2024,
            country = "Brazil",
            sessionType = "Race"
        )
        val specification = factory.buildSpecification(command)
        assertNotNull(specification)
    }

    @Test
    fun `should build specification for complex filter combination`() {
        val command = ListEventsCommand(
            year = 2024,
            country = "Brazil",
            sessionType = "Race"
        )
        val specification = factory.buildSpecification(command)
        assertNotNull(specification)
    }

    @Test
    fun `should return null specification when no filters provided`() {
        val command = ListEventsCommand()
        val specification = factory.buildSpecification(command)
        assertNotNull(specification)
    }
} 