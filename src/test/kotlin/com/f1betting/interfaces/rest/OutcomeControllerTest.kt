package com.f1betting.interfaces.rest

import com.f1betting.application.usecases.SimulateOutcomeUseCase
import com.f1betting.application.usecases.commands.SimulateOutcomeCommand
import com.f1betting.builders.DriverBuilder
import com.f1betting.builders.EventBuilder
import com.f1betting.builders.OutcomeBuilder
import com.f1betting.interfaces.rest.dto.request.SimulateOutcomeRequest
import com.f1betting.interfaces.rest.exception.GlobalExceptionHandler
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(OutcomeController::class)
@ContextConfiguration(classes = [OutcomeController::class, GlobalExceptionHandler::class])
class OutcomeControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var simulateOutcomeUseCase: SimulateOutcomeUseCase

    @Test
    fun `should simulate outcome successfully`() {
        // Given
        val driver = DriverBuilder.build {
            id = "driver1"
            externalId = "ext1"
            driverNumber = 1
            firstName = "Max"
            lastName = "Verstappen"
            fullName = "Max Verstappen"
        }
        
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
        
        val outcome = OutcomeBuilder.build {
            id = "outcome1"
            this.event = event
            winnerDriver = driver
        }

        val request = SimulateOutcomeRequest(winnerDriverId = "driver1")
        val expectedCommand = SimulateOutcomeCommand(eventId = "event1", winnerDriverId = "driver1")

        every { simulateOutcomeUseCase.execute(expectedCommand) } returns outcome

        // When & Then
        mockMvc.perform(post("/events/event1/outcome")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("outcome1"))
                .andExpect(jsonPath("$.event.id").value("event1"))
                .andExpect(jsonPath("$.winnerDriver.id").value("driver1"))
                .andExpect(jsonPath("$.winnerDriver.fullName").value("Max Verstappen"))

        verify { simulateOutcomeUseCase.execute(expectedCommand) }
    }

    @Test
    fun `should return bad request when invalid request data provided`() {
        // Given
        val request = SimulateOutcomeRequest(winnerDriverId = "")

        // When & Then
        mockMvc.perform(post("/events/event1/outcome")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest)

        verify(exactly = 0) { simulateOutcomeUseCase.execute(any()) }
    }
} 