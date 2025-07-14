package com.f1betting.application.usecases

import com.f1betting.application.usecases.commands.PlaceBetCommand
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

class PlaceBetUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var eventRepository: EventRepository
    private lateinit var driverRepository: DriverRepository
    private lateinit var eventDriverRepository: EventDriverRepository
    private lateinit var betRepository: BetRepository
    private lateinit var outcomeRepository: OutcomeRepository
    private lateinit var useCase: PlaceBetUseCase

    @BeforeEach
    fun setUp() {
        userRepository = mockk(relaxed = true)
        eventRepository = mockk(relaxed = true)
        driverRepository = mockk(relaxed = true)
        eventDriverRepository = mockk(relaxed = true)
        betRepository = mockk(relaxed = true)
        outcomeRepository = mockk(relaxed = true)
        useCase = PlaceBetUseCase(
            userRepository, eventRepository, driverRepository, eventDriverRepository, betRepository, outcomeRepository
        )
    }

    @Test
    fun `should place bet successfully`() {
        val user = createUser(balance = BigDecimal(100))
        val event = createEvent()
        val driver = createDriver()
        val eventDriver = createEventDriver(event, driver, odds = BigDecimal(2))
        val command = PlaceBetCommand(user.id!!, event.id!!, driver.id!!, BigDecimal(10))

        every { userRepository.findByUsername(user.username) } returns user
        every { eventRepository.findById(event.id!!) } returns event
        every { driverRepository.findById(driver.id!!) } returns driver
        every { outcomeRepository.existsByEventId(event.id!!) } returns false
        every { eventDriverRepository.findByEventIdAndDriverId(event.id!!, driver.id!!) } returns eventDriver
        every { betRepository.save(any()) } answers { firstArg() }
        every { userRepository.save(any()) } returns user

        val bet = useCase.execute(command)
        assertEquals(user, bet.user)
        assertEquals(event, bet.event)
        assertEquals(driver, bet.driver)
        assertEquals(BigDecimal(10), bet.amount)
        assertEquals(BigDecimal(2), bet.odds)
        assertEquals(BigDecimal(20), bet.potentialWinnings)
        assertEquals(BetStatus.PENDING, bet.status)
        verify { userRepository.save(user) }
        verify { betRepository.save(any()) }
    }

    @Test
    fun `should throw EntityNotFoundException when user not found`() {
        val command = PlaceBetCommand("userX", "event1", "driver1", BigDecimal.TEN)
        every { userRepository.findByUsername("userX") } returns null
        assertThrows(EntityNotFoundException::class.java) {
            useCase.execute(command)
        }
    }

    @Test
    fun `should throw EntityNotFoundException when event not found`() {
        val user = createUser()
        val command = PlaceBetCommand(user.id!!, "eventX", "driver1", BigDecimal.TEN)
        every { userRepository.findByUsername(user.username) } returns user
        every { eventRepository.findById("eventX") } returns null
        assertThrows(EntityNotFoundException::class.java) {
            useCase.execute(command)
        }
    }

    @Test
    fun `should throw EntityNotFoundException when driver not found`() {
        val user = createUser()
        val event = createEvent()
        val command = PlaceBetCommand(user.id!!, event.id!!, "driverX", BigDecimal.TEN)
        every { userRepository.findByUsername(user.username) } returns user
        every { eventRepository.findById(event.id!!) } returns event
        every { driverRepository.findById("driverX") } returns null
        assertThrows(EntityNotFoundException::class.java) {
            useCase.execute(command)
        }
    }

    @Test
    fun `should throw InsufficientBalanceException when user has not enough balance`() {
        val user = createUser(balance = BigDecimal(5))
        val event = createEvent()
        val driver = createDriver()
        val command = PlaceBetCommand(user.id!!, event.id!!, driver.id!!, BigDecimal(10))
        every { userRepository.findByUsername(user.username) } returns user
        every { eventRepository.findById(event.id!!) } returns event
        every { driverRepository.findById(driver.id!!) } returns driver
        every { outcomeRepository.existsByEventId(event.id!!) } returns false
        assertThrows(InsufficientBalanceException::class.java) {
            useCase.execute(command)
        }
    }

    @Test
    fun `should throw EventAlreadyFinishedException when event already has outcome`() {
        val user = createUser()
        val event = createEvent()
        val driver = createDriver()
        val command = PlaceBetCommand(user.id!!, event.id!!, driver.id!!, BigDecimal(10))
        every { userRepository.findByUsername(user.username) } returns user
        every { eventRepository.findById(event.id!!) } returns event
        every { driverRepository.findById(driver.id!!) } returns driver
        every { outcomeRepository.existsByEventId(event.id!!) } returns true
        assertThrows(EventAlreadyFinishedException::class.java) {
            useCase.execute(command)
        }
    }

    @Test
    fun `should throw DriverNotAvailableException when driver is not available for event`() {
        val user = createUser()
        val event = createEvent()
        val driver = createDriver()
        val command = PlaceBetCommand(user.id!!, event.id!!, driver.id!!, BigDecimal(10))
        every { userRepository.findByUsername(user.username) } returns user
        every { eventRepository.findById(event.id!!) } returns event
        every { driverRepository.findById(driver.id!!) } returns driver
        every { outcomeRepository.existsByEventId(event.id!!) } returns false
        every { eventDriverRepository.findByEventIdAndDriverId(event.id!!, driver.id!!) } returns null
        assertThrows(DriverNotAvailableException::class.java) {
            useCase.execute(command)
        }
    }

    private fun createUser(balance: BigDecimal = BigDecimal(100)): User =
        User(id = "user1", username = "user1", email = "test@test.com", balance = balance, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())

    private fun createEvent(): Event =
        Event(id = "event1", externalId = "ext1", meetingKey = 1, sessionKey = 1, year = 2024, countryName = "BR", circuitShortName = "BR", sessionName = "GP Brasil", sessionType = "Race", dateStart = LocalDateTime.now(), createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())

    private fun createDriver(): Driver =
        Driver(id = "driver1", externalId = "d1", driverNumber = 1, firstName = "A", lastName = "B", fullName = "A B", broadcastName = null, nameAcronym = null, countryCode = null, teamName = null, teamColour = null, headshotUrl = null, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())

    private fun createEventDriver(event: Event, driver: Driver, odds: BigDecimal): EventDriver =
        EventDriver(id = "ed1", event = event, driver = driver, odds = odds, createdAt = LocalDateTime.now())
} 