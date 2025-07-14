package com.f1betting.interfaces.rest

import com.f1betting.application.usecases.GetEventDriversUseCase
import com.f1betting.application.usecases.GetEventUseCase
import com.f1betting.application.usecases.ListEventsUseCase
import com.f1betting.application.usecases.commands.ListEventsCommand
import com.f1betting.builders.DriverBuilder
import com.f1betting.builders.EventBuilder
import com.f1betting.builders.EventDriverBuilder
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import java.math.BigDecimal
import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(EventController::class)
@ContextConfiguration(classes = [EventController::class])
class EventControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var listEventsUseCase: ListEventsUseCase

    @MockkBean
    private lateinit var getEventUseCase: GetEventUseCase

    @MockkBean
    private lateinit var getEventDriversUseCase: GetEventDriversUseCase

    @Test
    fun `should return all events when no filters provided`() {
        // Given
        val event = EventBuilder.build {
            id = "event1"
            externalId = "ext1"
            meetingKey = 1
            sessionKey = 1
            year = 2024
            countryName = "Monaco"
            circuitShortName = "MON"
            sessionName = "Monaco Grand Prix"
            sessionType = "RACE"
            dateStart = LocalDateTime.now()
        }

        val events = listOf(event)

        val command = ListEventsCommand(
            year = null,
            country = null,
            sessionType = null
        )

        every { listEventsUseCase.execute(command) } returns events

        // When & Then
        mockMvc.perform(get("/events"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value("event1"))
            .andExpect(jsonPath("$[0].countryName").value("Monaco"))

        verify { listEventsUseCase.execute(command) }
    }

    @Test
    fun `should return event by id`() {
        // Given
        val event = EventBuilder.build {
            id = "event1"
            externalId = "ext1"
            meetingKey = 1
            sessionKey = 1
            year = 2024
            countryName = "Monaco"
            circuitShortName = "MON"
            sessionName = "Monaco Grand Prix"
            sessionType = "RACE"
            dateStart = LocalDateTime.now()
        }

        every { getEventUseCase.execute("event1") } returns event

        // When & Then
        mockMvc.perform(get("/events/event1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value("event1"))
            .andExpect(jsonPath("$.countryName").value("Monaco"))

        verify { getEventUseCase.execute("event1") }
    }

    @Test
    fun `should return event drivers`() {
        // Given
        val event = EventBuilder.build {
            id = "event1"
            externalId = "ext1"
            meetingKey = 1
            sessionKey = 1
            year = 2024
            countryName = "Monaco"
            circuitShortName = "MON"
            sessionName = "Monaco Grand Prix"
            sessionType = "RACE"
            dateStart = LocalDateTime.now()
        }

        val driver = DriverBuilder.build {
            id = "driver1"
            externalId = "d1"
            driverNumber = 1
            firstName = "Max"
            lastName = "Verstappen"
            fullName = "Max Verstappen"
        }

        val eventDriver = EventDriverBuilder.build {
            id = "ed1"
            this.event = event
            this.driver = driver
            odds = BigDecimal("2.5")
            createdAt = LocalDateTime.now()
        }

        every { getEventDriversUseCase.execute("event1") } returns listOf(eventDriver)

        // When & Then
        mockMvc.perform(get("/events/event1/drivers"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value("ed1"))
            .andExpect(jsonPath("$[0].driver.fullName").value("Max Verstappen"))
            .andExpect(jsonPath("$[0].odds").value(2.5))

        verify { getEventDriversUseCase.execute("event1") }
    }

    @Test
    fun `should return filtered events`() {
        // Given
        val event = EventBuilder.build {
            id = "event1"
            externalId = "ext1"
            meetingKey = 1
            sessionKey = 1
            year = 2024
            countryName = "Monaco"
            circuitShortName = "MON"
            sessionName = "Monaco Grand Prix"
            sessionType = "RACE"
            dateStart = LocalDateTime.now()
        }

        val command = ListEventsCommand(
            year = 2024,
            country = "Monaco",
            sessionType = "RACE"
        )

        every { listEventsUseCase.execute(command) } returns listOf(event)

        // When & Then
        mockMvc.perform(get("/events?year=2024&country=Monaco&sessionType=RACE"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].year").value(2024))
            .andExpect(jsonPath("$[0].countryName").value("Monaco"))

        verify { listEventsUseCase.execute(command) }
    }
}