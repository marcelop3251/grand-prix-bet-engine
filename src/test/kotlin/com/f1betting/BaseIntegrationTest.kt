package com.f1betting

import com.f1betting.infrastructure.persistence.entities.*
import com.f1betting.infrastructure.persistence.repositories.spring.BetJpaRepository
import com.f1betting.infrastructure.persistence.repositories.spring.DriverJpaRepository
import com.f1betting.infrastructure.persistence.repositories.spring.EventDriverJpaRepository
import com.f1betting.infrastructure.persistence.repositories.spring.EventJpaRepository
import com.f1betting.infrastructure.persistence.repositories.spring.OutcomeJpaRepository
import com.f1betting.infrastructure.persistence.repositories.spring.UserJpaRepository
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.annotation.DirtiesContext
import jakarta.persistence.EntityManager
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
abstract class BaseIntegrationTest {

    @LocalServerPort
    protected var port: Int = 0

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    @Autowired
    protected lateinit var userJpaRepository: UserJpaRepository

    @Autowired
    protected lateinit var eventJpaRepository: EventJpaRepository

    @Autowired
    protected lateinit var driverJpaRepository: DriverJpaRepository

    @Autowired
    protected lateinit var eventDriverJpaRepository: EventDriverJpaRepository

    @Autowired
    protected lateinit var betJpaRepository: BetJpaRepository

    @Autowired
    protected lateinit var outcomeJpaRepository: OutcomeJpaRepository

    @Autowired
    protected lateinit var entityManager: EntityManager

    protected lateinit var wireMockServer: WireMockServer

    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("openf1.base-url") { "http://localhost:8089" }
        }
    }

    @BeforeEach
    fun setUp() {
        // Start WireMock server for OpenF1 API
        wireMockServer = WireMockServer(wireMockConfig().port(8089))
        wireMockServer.start()
        
        // Clear all repositories
        clearDatabase()
        
        // Setup test data
        setupTestData()
    }

    @AfterEach
    fun tearDown() {
        wireMockServer.stop()
    }

    protected fun clearDatabase() {
        betJpaRepository.deleteAll()
        outcomeJpaRepository.deleteAll()
        eventDriverJpaRepository.deleteAll()
        eventJpaRepository.deleteAll()
        driverJpaRepository.deleteAll()
        userJpaRepository.deleteAll()
    }

    protected fun setupTestData() {
        // Create test user
        val userEntity = UserEntity(
            id = UUID.randomUUID().toString(),
            username = "user1",
            email = "test@example.com",
            balance = BigDecimal("1000.00")
        )
         userJpaRepository.saveAndFlush(userEntity)

        // Create test drivers
        val driver1 = DriverEntity(
            id = UUID.randomUUID().toString(),
            externalId = "1",
            driverNumber = 44,
            firstName = "Lewis",
            lastName = "Hamilton",
            fullName = "Lewis Hamilton",
            broadcastName = "L. HAMILTON",
            nameAcronym = "HAM",
            countryCode = "GBR",
            teamName = "Mercedes",
            teamColour = "00D2BE"
        )
        val driver2 = DriverEntity(
            id = UUID.randomUUID().toString(),
            externalId = "2",
            driverNumber = 1,
            firstName = "Max",
            lastName = "Verstappen",
            fullName = "Max Verstappen",
            broadcastName = "M. VERSTAPPEN",
            nameAcronym = "VER",
            countryCode = "NLD",
            teamName = "Red Bull Racing",
            teamColour = "0600EF"
        )
        driverJpaRepository.saveAllAndFlush(listOf(driver1, driver2))
        val savedDrivers = driverJpaRepository.findAll()
        val savedDriver1 = savedDrivers[0]
        val savedDriver2 = savedDrivers[1]

        // Create test event
        val eventEntity = EventEntity(
            id = UUID.randomUUID().toString(),
            externalId = "123",
            meetingKey = 1234,
            sessionKey = 5678,
            year = 2024,
            countryName = "Monaco",
            circuitShortName = "MON",
            sessionName = "Monaco Grand Prix",
            sessionType = "Race",
            dateStart = ZonedDateTime.now().plusDays(1).toLocalDateTime(),
            dateEnd = ZonedDateTime.now().plusDays(1).plusHours(2).toLocalDateTime(),
            location = "Monte Carlo"
        )
        val savedEvent = eventJpaRepository.saveAndFlush(eventEntity)

        // Create event-driver relationships using the saved entities
        val eventDriver1 = EventDriverEntity(
            id = UUID.randomUUID().toString(),
            event = savedEvent,
            driver = savedDriver1,
            odds = BigDecimal("2.5")
        )
        val eventDriver2 = EventDriverEntity(
            id = UUID.randomUUID().toString(),
            event = savedEvent,
            driver = savedDriver2,
            odds = BigDecimal("3.0")
        )
        eventDriverJpaRepository.saveAllAndFlush(listOf(eventDriver1, eventDriver2))
    }

    protected fun getBaseUrl(): String = "http://localhost:$port/api/v1"

    protected fun getTestUser(): UserEntity = userJpaRepository.findAll().first()
    protected fun getTestEvent(): EventEntity = eventJpaRepository.findAll().first()
    protected fun getTestDrivers(): List<DriverEntity> = driverJpaRepository.findAll()
    protected fun getTestEventDrivers(): List<EventDriverEntity> = eventDriverJpaRepository.findAll()
} 