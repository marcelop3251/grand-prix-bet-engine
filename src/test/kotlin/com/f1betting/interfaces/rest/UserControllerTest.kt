package com.f1betting.interfaces.rest

import com.f1betting.application.usecases.GetCurrentUserUseCase
import com.f1betting.application.usecases.ListUserBetsUseCase
import com.f1betting.builders.BetBuilder
import com.f1betting.builders.DriverBuilder
import com.f1betting.builders.EventBuilder
import com.f1betting.builders.UserBuilder
import com.f1betting.domain.entities.BetStatus
import com.fasterxml.jackson.databind.ObjectMapper
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

@WebMvcTest(UserController::class)
@ContextConfiguration(classes = [UserController::class])
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    @MockkBean
    private lateinit var listUserBetsUseCase: ListUserBetsUseCase

    @Test
    fun `should return user profile when user exists`() {
        // Given
        val user = UserBuilder.build {
            id = "user1"
            username = "testuser"
            email = "test@example.com"
            balance = BigDecimal("1000.00")
        }

        every { getCurrentUserUseCase.execute("user1") } returns user

        // When & Then
        mockMvc.perform(get("/users/me")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("user1"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.balance").value(1000.00))

        verify { getCurrentUserUseCase.execute("user1") }
    }

    @Test
    fun `should return user bets when requested`() {
        // Given
        val user = UserBuilder.build {
            id = "user1"
            username = "testuser"
            email = "test@example.com"
            balance = BigDecimal("1000.00")
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

        val driver = DriverBuilder.build {
            id = "driver1"
            externalId = "ext1"
            driverNumber = 1
            firstName = "Max"
            lastName = "Verstappen"
            fullName = "Max Verstappen"
        }

        val bets = listOf(
            BetBuilder.build {
                id = "bet1"
                this.user = user
                this.event = event
                this.driver = driver
                amount = BigDecimal("100.00")
                odds = BigDecimal("2.5")
                potentialWinnings = BigDecimal("250.00")
                status = BetStatus.PENDING
            }
        )

        every { listUserBetsUseCase.execute("user1") } returns bets

        // When & Then
        mockMvc.perform(get("/users/me/bets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("bet1"))
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[0].amount").value(100.00))

        verify { listUserBetsUseCase.execute("user1") }
    }

    @Test
    fun `should return empty list when user has no bets`() {
        // Given
        every { listUserBetsUseCase.execute("user1") } returns emptyList()

        // When & Then
        mockMvc.perform(get("/users/me/bets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty)

        verify { listUserBetsUseCase.execute("user1") }
    }
} 