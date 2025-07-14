package com.f1betting.application.usecases

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

class GetUserBetsUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var betRepository: BetRepository
    private lateinit var useCase: GetUserBetsUseCase

    @BeforeEach
    fun setUp() {
        userRepository = mockk(relaxed = true)
        betRepository = mockk(relaxed = true)
        useCase = GetUserBetsUseCase(userRepository, betRepository)
    }

    @Test
    fun `should return user bets when user exists`() {
        // Given
        val username = "testuser"
        val user = createUser(username)
        val event = createEvent()
        val driver = createDriver()
        val bets = listOf(
            createBet("bet1", user, event, driver),
            createBet("bet2", user, event, driver)
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
        val user = createUser(username)
        
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

    private fun createUser(username: String = "testuser"): User =
        User(
            id = "user1",
            username = username,
            email = "test@test.com",
            balance = BigDecimal(100),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

    private fun createEvent(): Event =
        Event(
            id = "event1",
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

    private fun createDriver(): Driver =
        Driver(
            id = "driver1",
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

    private fun createBet(id: String, user: User, event: Event, driver: Driver): Bet =
        Bet(
            id = id,
            user = user,
            event = event,
            driver = driver,
            amount = BigDecimal(10),
            odds = BigDecimal(2),
            potentialWinnings = BigDecimal(20),
            status = BetStatus.PENDING,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
} 