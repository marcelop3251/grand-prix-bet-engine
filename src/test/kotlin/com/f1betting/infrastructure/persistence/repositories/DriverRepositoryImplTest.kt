package com.f1betting.infrastructure.persistence.repositories

import com.f1betting.builders.DriverBuilder
import com.f1betting.infrastructure.persistence.entities.DriverEntity
import com.f1betting.infrastructure.persistence.repositories.spring.DriverJpaRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class DriverRepositoryImplTest {
    private lateinit var driverJpaRepository: DriverJpaRepository
    private lateinit var driverRepository: DriverRepositoryImpl

    @BeforeEach
    fun setUp() {
        driverJpaRepository = mockk(relaxed = true)
        driverRepository = DriverRepositoryImpl(driverJpaRepository)
    }

    @Test
    fun `findByExternalId should return mapped driver`() {
        val driverEntity = DriverEntity.fromDomain(DriverBuilder.build())
        every { driverJpaRepository.findByExternalId("d1") } returns driverEntity
        val result = driverRepository.findByExternalId("d1")
        assertNotNull(result)
        assertEquals(driverEntity.id, result?.id)
    }

    @Test
    fun `save should persist and return mapped driver`() {
        val driver = DriverBuilder.build()
        val driverEntity = DriverEntity.fromDomain(driver)
        every { driverJpaRepository.save(any()) } returns driverEntity
        val result = driverRepository.save(driver)
        assertEquals(driver.id, result.id)
    }

    @Test
    fun `findById should return mapped driver if present`() {
        val driverEntity = DriverEntity.fromDomain(DriverBuilder.build())
        every { driverJpaRepository.findById("driver1") } returns Optional.of(driverEntity)
        val result = driverRepository.findById("driver1")
        assertNotNull(result)
        assertEquals(driverEntity.id, result?.id)
    }

    @Test
    fun `findById should return null if not present`() {
        every { driverJpaRepository.findById("driver2") } returns Optional.empty()
        val result = driverRepository.findById("driver2")
        assertNull(result)
    }
} 