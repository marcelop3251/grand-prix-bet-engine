package com.f1betting.application.usecases

import com.f1betting.application.usecases.commands.SimulateOutcomeCommand
import com.f1betting.domain.entities.*
import com.f1betting.domain.exceptions.*
import com.f1betting.domain.repositories.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class SimulateOutcomeUseCaseTest {
    private lateinit var eventRepository: EventRepository
    private lateinit var driverRepository: DriverRepository
    private lateinit var eventDriverRepository: EventDriverRepository
    private lateinit var betRepository: BetRepository
    private lateinit var outcomeRepository: OutcomeRepository
    private lateinit var userRepository: UserRepository
    private lateinit var useCase: SimulateOutcomeUseCase

    @BeforeEach
    fun setUp() {
        eventRepository = mockk(relaxed = true)
        driverRepository = mockk(relaxed = true)
        eventDriverRepository = mockk(relaxed = true)
        betRepository = mockk(relaxed = true)
        outcomeRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        useCase = SimulateOutcomeUseCase(
            eventRepository, driverRepository, eventDriverRepository, betRepository, outcomeRepository, userRepository
        )
    }

    @Test
    fun `should simulate outcome successfully`() {
        val event = createEvent()
        val winnerDriver = createDriver("winner1")
        val command = SimulateOutcomeCommand(event.id!!, winnerDriver.id!!)
        val outcome = createOutcome(event, winnerDriver)

        every { eventRepository.findById(event.id!!) } returns event
        every { driverRepository.findById(winnerDriver.id!!) } returns winnerDriver
        every { eventDriverRepository.existsByEventIdAndDriverId(event.id!!, winnerDriver.id!!) } returns true
        every { outcomeRepository.existsByEventId(event.id!!) } returns false
        every { outcomeRepository.save(any()) } returns outcome
        every { betRepository.findByEventId(event.id!!) } returns emptyList()

        val result = useCase.execute(command)

        assertEquals(event, result.event)
        assertEquals(winnerDriver, result.winnerDriver)
        verify { outcomeRepository.save(any()) }
    }

    @Test
    fun `should process winning bets correctly`() {
        val event = createEvent()
        val winnerDriver = createDriver("winner1")
        val loserDriver = createDriver("loser1")
        val user = createUser()
        val winningBet = createBet(event, winnerDriver, BetStatus.PENDING, user)
        val losingBet = createBet(event, loserDriver, BetStatus.PENDING, user)
        val command = SimulateOutcomeCommand(event.id!!, winnerDriver.id!!)
        val outcome = createOutcome(event, winnerDriver)

        every { eventRepository.findById(event.id!!) } returns event
        every { driverRepository.findById(winnerDriver.id!!) } returns winnerDriver
        every { eventDriverRepository.existsByEventIdAndDriverId(event.id!!, winnerDriver.id!!) } returns true
        every { outcomeRepository.existsByEventId(event.id!!) } returns false
        every { outcomeRepository.save(any()) } returns outcome
        every { betRepository.findByEventId(event.id!!) } returns listOf(winningBet, losingBet)
        every { betRepository.save(any()) } answers { firstArg() }
        every { userRepository.save(any()) } returns user

        val result = useCase.execute(command)

        assertEquals(event, result.event)
        assertEquals(winnerDriver, result.winnerDriver)
        verify { betRepository.save(winningBet) }
        verify { betRepository.save(losingBet) }
        verify { userRepository.save(user) }
    }

    @Test
    fun `should throw EntityNotFoundException when event not found`() {
        val command = SimulateOutcomeCommand("eventX", "driver1")
        every { eventRepository.findById("eventX") } returns null

        assertThrows(EntityNotFoundException::class.java) {
            useCase.execute(command)
        }
    }

    @Test
    fun `should throw EntityNotFoundException when driver not found`() {
        val event = createEvent()
        val command = SimulateOutcomeCommand(event.id!!, "driverX")
        every { eventRepository.findById(event.id!!) } returns event
        every { driverRepository.findById("driverX") } returns null

        assertThrows(EntityNotFoundException::class.java) {
            useCase.execute(command)
        }
    }

    @Test
    fun `should throw DriverNotAvailableException when driver not available for event`() {
        val event = createEvent()
        val winnerDriver = createDriver("winner1")
        val command = SimulateOutcomeCommand(event.id!!, winnerDriver.id!!)

        every { eventRepository.findById(event.id!!) } returns event
        every { driverRepository.findById(winnerDriver.id!!) } returns winnerDriver
        every { eventDriverRepository.existsByEventIdAndDriverId(event.id!!, winnerDriver.id!!) } returns false

        assertThrows(DriverNotAvailableException::class.java) {
            useCase.execute(command)
        }
    }

    @Test
    fun `should throw OutcomeAlreadyExistsException when outcome already exists`() {
        val event = createEvent()
        val winnerDriver = createDriver("winner1")
        val command = SimulateOutcomeCommand(event.id!!, winnerDriver.id!!)

        every { eventRepository.findById(event.id!!) } returns event
        every { driverRepository.findById(winnerDriver.id!!) } returns winnerDriver
        every { eventDriverRepository.existsByEventIdAndDriverId(event.id!!, winnerDriver.id!!) } returns true
        every { outcomeRepository.existsByEventId(event.id!!) } returns true

        assertThrows(OutcomeAlreadyExistsException::class.java) {
            useCase.execute(command)
        }
    }

    private fun createEvent(): Event =
        Event(id = "event1", externalId = "ext1", meetingKey = 1, sessionKey = 1, year = 2024, countryName = "BR", circuitShortName = "BR", sessionName = "GP Brasil", sessionType = "Race", dateStart = LocalDateTime.now(), createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())

    private fun createDriver(id: String = "driver1"): Driver =
        Driver(id = id, externalId = "d1", driverNumber = 1, firstName = "A", lastName = "B", fullName = "A B", broadcastName = null, nameAcronym = null, countryCode = null, teamName = null, teamColour = null, headshotUrl = null, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())

    private fun createBet(event: Event, driver: Driver, status: BetStatus, user: User = createUser()): Bet =
        Bet(id = "bet1", user = user, event = event, driver = driver, amount = BigDecimal(10), odds = BigDecimal(2), potentialWinnings = BigDecimal(20), status = status, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())

    private fun createUser(): User =
        User(id = "user1", username = "test", email = "test@test.com", balance = BigDecimal(100), createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())

    private fun createOutcome(event: Event, winnerDriver: Driver): Outcome =
        Outcome(id = "outcome1", event = event, winnerDriver = winnerDriver, createdAt = LocalDateTime.now())
} 