package com.f1betting.infrastructure.gateway.openf1

import com.f1betting.infrastructure.gateway.openf1.dto.OpenF1DriverDto
import com.f1betting.infrastructure.gateway.openf1.dto.OpenF1SessionDto
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.time.ZonedDateTime

class OpenF1ClientTest {

    private lateinit var wireMockServer: WireMockServer
    private lateinit var openF1Client: OpenF1Client

    @BeforeEach
    fun setUp() {
        wireMockServer = WireMockServer(wireMockConfig().port(8089))
        wireMockServer.start()
        
        // Create WebClient pointing to WireMock server
        val webClient = WebClient.builder()
            .baseUrl("http://localhost:8089")
            .codecs { configurer ->
                configurer.defaultCodecs().maxInMemorySize(1024 * 1024) // 1MB
            }
            .build()
        
        openF1Client = OpenF1Client(webClient)
    }

    @AfterEach
    fun tearDown() {
        wireMockServer.stop()
    }

    @Test
    fun `should get sessions successfully`() = runBlocking {
        // Given
        val mockResponse = """
            [
                {
                    "session_key": 9158,
                    "meeting_key": 1217,
                    "session_name": "Monaco Grand Prix",
                    "session_type": "Race",
                    "date_start": "2024-05-26T13:00:00+00:00",
                    "date_end": "2024-05-26T15:00:00+00:00",
                    "year": 2024,
                    "country_name": "Monaco",
                    "circuit_short_name": "MON",
                    "location": "Monte Carlo"
                }
            ]
        """.trimIndent()

        wireMockServer.stubFor(
            get(urlEqualTo("/sessions"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponse)
                )
        )

        // When
        val sessions = openF1Client.getSessions()

        // Then
        assertEquals(1, sessions.size)
        val session = sessions[0]
        assertEquals(9158, session.sessionKey)
        assertEquals(1217, session.meetingKey)
        assertEquals("Monaco Grand Prix", session.sessionName)
        assertEquals("Race", session.sessionType)
        assertEquals(2024, session.year)
        assertEquals("Monaco", session.countryName)
        assertEquals("MON", session.circuitShortName)
        assertEquals("Monte Carlo", session.location)
        
        wireMockServer.verify(getRequestedFor(urlEqualTo("/sessions")))
    }

    @Test
    fun `should get sessions with filters`() = runBlocking {
        // Given
        val mockResponse = """
            [
                {
                    "session_key": 9158,
                    "meeting_key": 1217,
                    "session_name": "Monaco Grand Prix",
                    "session_type": "Race",
                    "date_start": "2024-05-26T13:00:00+00:00",
                    "date_end": "2024-05-26T15:00:00+00:00",
                    "year": 2024,
                    "country_name": "Monaco",
                    "circuit_short_name": "MON",
                    "location": "Monte Carlo"
                }
            ]
        """.trimIndent()

        wireMockServer.stubFor(
            get(urlPathEqualTo("/sessions"))
                .withQueryParam("year", equalTo("2024"))
                .withQueryParam("country_name", equalTo("Monaco"))
                .withQueryParam("session_type", equalTo("Race"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponse)
                )
        )

        // When
        val sessions = openF1Client.getSessions(year = 2024, country = "Monaco", sessionType = "Race")

        // Then
        assertEquals(1, sessions.size)
        assertEquals("Monaco Grand Prix", sessions[0].sessionName)
        
        wireMockServer.verify(
            getRequestedFor(urlPathEqualTo("/sessions"))
                .withQueryParam("year", equalTo("2024"))
                .withQueryParam("country_name", equalTo("Monaco"))
                .withQueryParam("session_type", equalTo("Race"))
        )
    }

    @Test
    fun `should get drivers by session key`() = runBlocking {
        // Given
        val mockResponse = """
            [
                {
                    "driver_number": 1,
                    "first_name": "Max",
                    "last_name": "Verstappen",
                    "full_name": "Max Verstappen",
                    "broadcast_name": "M. VERSTAPPEN",
                    "name_acronym": "VER",
                    "country_code": "NLD",
                    "team_name": "Red Bull Racing",
                    "team_colour": "3671C6",
                    "headshot_url": "https://example.com/verstappen.jpg",
                    "session_key": 9158
                }
            ]
        """.trimIndent()

        wireMockServer.stubFor(
            get(urlPathEqualTo("/drivers"))
                .withQueryParam("session_key", equalTo("9158"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponse)
                )
        )

        // When
        val drivers = openF1Client.getDrivers(9158)

        // Then
        assertEquals(1, drivers.size)
        val driver = drivers[0]
        assertEquals(1, driver.driverNumber)
        assertEquals("Max", driver.firstName)
        assertEquals("Verstappen", driver.lastName)
        assertEquals("Max Verstappen", driver.fullName)
        assertEquals("M. VERSTAPPEN", driver.broadcastName)
        assertEquals("VER", driver.nameAcronym)
        assertEquals("NLD", driver.countryCode)
        assertEquals("Red Bull Racing", driver.teamName)
        assertEquals("3671C6", driver.teamColour)
        assertEquals("https://example.com/verstappen.jpg", driver.headshotUrl)
        assertEquals(9158, driver.sessionKey)
        
        wireMockServer.verify(
            getRequestedFor(urlPathEqualTo("/drivers"))
                .withQueryParam("session_key", equalTo("9158"))
        )
    }

    @Test
    fun `should get drivers by year`() = runBlocking {
        // Given
        val mockResponse = """
            [
                {
                    "driver_number": 1,
                    "first_name": "Max",
                    "last_name": "Verstappen",
                    "full_name": "Max Verstappen",
                    "broadcast_name": "M. VERSTAPPEN",
                    "name_acronym": "VER",
                    "country_code": "NLD",
                    "team_name": "Red Bull Racing",
                    "team_colour": "3671C6",
                    "headshot_url": "https://example.com/verstappen.jpg",
                    "session_key": 9158
                }
            ]
        """.trimIndent()

        wireMockServer.stubFor(
            get(urlPathEqualTo("/drivers"))
                .withQueryParam("year", equalTo("2024"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponse)
                )
        )

        // When
        val drivers = openF1Client.getDriversByYear(2024)

        // Then
        assertEquals(1, drivers.size)
        assertEquals("Max Verstappen", drivers[0].fullName)
        
        wireMockServer.verify(
            getRequestedFor(urlPathEqualTo("/drivers"))
                .withQueryParam("year", equalTo("2024"))
        )
    }

    @Test
    fun `should handle empty response`() = runBlocking {
        // Given
        wireMockServer.stubFor(
            get(urlEqualTo("/sessions"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")
                )
        )

        // When
        val sessions = openF1Client.getSessions()

        // Then
        assertTrue(sessions.isEmpty())
    }

    @Test
    fun `should handle API error response`() = runBlocking {
        // Given
        wireMockServer.stubFor(
            get(urlEqualTo("/sessions"))
                .willReturn(
                    aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""{"error": "Internal Server Error"}""")
                )
        )

        // When & Then
        assertThrows(WebClientResponseException::class.java) {
            runBlocking {
                openF1Client.getSessions()
            }
        }
    }

    @Test
    fun `should handle network timeout`() = runBlocking {
        // Given
        wireMockServer.stubFor(
            get(urlEqualTo("/sessions"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")
                        .withFixedDelay(10000) // 10 second delay
                )
        )

        // When & Then
        assertThrows(Exception::class.java) {
            runBlocking {
                openF1Client.getSessions()
            }
        }
    }
} 