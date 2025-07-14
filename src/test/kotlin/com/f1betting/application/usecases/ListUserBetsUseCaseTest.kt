package com.f1betting.application.usecases

import com.f1betting.builders.BetBuilder
import com.f1betting.builders.DriverBuilder
import com.f1betting.builders.EventBuilder
import com.f1betting.builders.UserBuilder
import com.f1betting.domain.entities.Bet
import com.f1betting.domain.entities.BetStatus
import com.f1betting.domain.entities.Driver
import com.f1betting.domain.entities.Event
import com.f1betting.domain.entities.User
import com.f1betting.domain.exceptions.EntityNotFoundException
import com.f1betting.domain.repositories.BetRepository
import com.f1betting.domain.repositories.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class ListUserBetsUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var betRepository: BetRepository
    private lateinit var useCase: ListUserBetsUseCase

    @BeforeEach
    fun setUp() {
        userRepository = mockk(relaxed = true)
        betRepository = mockk(relaxed = true)
        useCase = ListUserBetsUseCase(userRepository, betRepository)
    }

    @Test
    fun `should return user bets when user exists`() {
        // Given
        val username = "testuser"
        val user = UserBuilder.build {
            this.username = username
        }
        val event = EventBuilder.build { sessionName = "Monaco Grand Prix" }
        val driver = DriverBuilder.build { fullName = "Max Verstappen" }
        
        val bets = listOf(
            BetBuilder.build { 
                id = "bet1"
                this.user = user
                this.event = event
                this.driver = driver
                amount = BigDecimal("50.00")
            },
            BetBuilder.build { 
                id = "bet2"
                this.user = user
                this.event = event
                this.driver = driver
                amount = BigDecimal("30.00")
                status = BetStatus.WON
            }
        )
        
        every { userRepository.findByUsername(username) } returns user
        every { betRepository.findByUserId(user.id!!) } returns bets

        // When
        val result = useCase.execute(username)

        // Then
        assertEquals(bets, result)
        assertEquals(2, result.size)
    }

    @Test
    fun `should return empty list when user exists but has no bets`() {
        // Given
        val username = "testuser"
        val user = UserBuilder.build { this.username = username }
        
        every { userRepository.findByUsername(username) } returns user
        every { betRepository.findByUserId(user.id!!) } returns emptyList()

        // When
        val result = useCase.execute(username)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should throw EntityNotFoundException when user not found`() {
        // Given
        val username = "nonexistent"
        
        every { userRepository.findByUsername(username) } returns null

        // When & Then
        assertThrows(EntityNotFoundException::class.java) {
            useCase.execute(username)
        }
    }
} 