package com.f1betting.interfaces.rest

import com.f1betting.application.usecases.GetBetUseCase
import com.f1betting.application.usecases.ListUserBetsUseCase
import com.f1betting.application.usecases.PlaceBetUseCase
import com.f1betting.application.usecases.commands.PlaceBetCommand
import com.f1betting.builders.BetBuilder
import com.f1betting.builders.DriverBuilder
import com.f1betting.builders.EventBuilder
import com.f1betting.builders.UserBuilder
import com.f1betting.domain.entities.BetStatus
import com.f1betting.interfaces.rest.dto.request.PlaceBetRequest
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BetController::class)
@ContextConfiguration(classes = [BetController::class])
class BetControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var placeBetUseCase: PlaceBetUseCase

    @MockkBean
    private lateinit var listUserBetsUseCase: ListUserBetsUseCase

    @MockkBean
    private lateinit var getBetUseCase: GetBetUseCase

    @Test
    fun `should place bet successfully`() {
        // Given
        val user = UserBuilder.build {
            id = "user1"
            username = "testuser"
            email = "test@example.com"
        }
        
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
        
        val bet = BetBuilder.build {
            id = "bet1"
            this.user = user
            this.event = event
            this.driver = driver
            amount = BigDecimal("100.00")
            odds = BigDecimal("2.5")
            potentialWinnings = BigDecimal("250.00")
            status = BetStatus.PENDING
        }

        val request = PlaceBetRequest(
            eventId = "event1",
            driverId = "driver1",
            amount = BigDecimal("100.00")
        )

        val expectedCommand = PlaceBetCommand(
            userId = "user1",
            eventId = "event1",
            driverId = "driver1",
            amount = BigDecimal("100.00")
        )

        every { placeBetUseCase.execute(expectedCommand) } returns bet

        // When & Then
        mockMvc.perform(post("/bets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("bet1"))
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.amount").value(100.00))

        verify { placeBetUseCase.execute(expectedCommand) }
    }

    @Test
    fun `should list user bets successfully`() {
        // Given
        val user = UserBuilder.build {
            id = "user1"
            username = "testuser"
            email = "test@example.com"
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
            },
            BetBuilder.build {
                id = "bet2"
                this.user = user
                this.event = event
                this.driver = driver
                amount = BigDecimal("50.00")
                odds = BigDecimal("3.0")
                potentialWinnings = BigDecimal("150.00")
                status = BetStatus.PENDING
            }
        )

        every { listUserBetsUseCase.execute("user1") } returns bets

        // When & Then
        mockMvc.perform(get("/bets"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("bet1"))
                .andExpect(jsonPath("$[1].id").value("bet2"))

        verify { listUserBetsUseCase.execute("user1") }
    }

    @Test
    fun `should get bet by id successfully`() {
        // Given
        val user = UserBuilder.build {
            id = "user1"
            username = "testuser"
            email = "test@example.com"
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
        
        val bet = BetBuilder.build {
            id = "bet1"
            this.user = user
            this.event = event
            this.driver = driver
            amount = BigDecimal("100.00")
            odds = BigDecimal("2.5")
            potentialWinnings = BigDecimal("250.00")
            status = BetStatus.PENDING
        }

        every { getBetUseCase.execute("bet1") } returns bet

        // When & Then
        mockMvc.perform(get("/bets/bet1"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("bet1"))
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.amount").value(100.00))

        verify { getBetUseCase.execute("bet1") }
    }
} 