package com.f1betting.infrastructure.services

import com.f1betting.domain.repositories.DriverRepository
import com.f1betting.domain.repositories.EventDriverRepository
import com.f1betting.domain.repositories.EventRepository
import com.f1betting.infrastructure.gateway.openf1.OpenF1Client
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DataSyncServiceTest {
    private lateinit var openF1Client: OpenF1Client
    private lateinit var eventRepository: EventRepository
    private lateinit var driverRepository: DriverRepository
    private lateinit var eventDriverRepository: EventDriverRepository
    private lateinit var dataSyncService: DataSyncService

    @BeforeEach
    fun setUp() {
        openF1Client = mockk(relaxed = true)
        eventRepository = mockk(relaxed = true)
        driverRepository = mockk(relaxed = true)
        eventDriverRepository = mockk(relaxed = true)
        dataSyncService = DataSyncService(openF1Client, eventRepository, driverRepository, eventDriverRepository)
    }

    @Test
    fun `should instantiate DataSyncService`() {
        assertNotNull(dataSyncService)
    }
} 