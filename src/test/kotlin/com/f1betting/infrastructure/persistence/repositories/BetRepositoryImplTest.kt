package com.f1betting.infrastructure.persistence.repositories

import com.f1betting.builders.BetBuilder
import com.f1betting.infrastructure.persistence.repositories.spring.BetJpaRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class BetRepositoryImplTest {
    private lateinit var betJpaRepository: BetJpaRepository
    private lateinit var betRepository: BetRepositoryImpl

    @BeforeEach
    fun setUp() {
        betJpaRepository = mockk(relaxed = true)
        betRepository = BetRepositoryImpl(betJpaRepository)
    }

    @Test
    fun `findByUserId should return mapped bets`() {
        val betEntity = com.f1betting.infrastructure.persistence.entities.BetEntity.fromDomain(BetBuilder.build())
        every { betJpaRepository.findByUserId("user1") } returns listOf(betEntity)
        val result = betRepository.findByUserId("user1")
        assertEquals(1, result.size)
        assertEquals(betEntity.id, result[0].id)
    }

    @Test
    fun `findByEventId should return mapped bets`() {
        val betEntity = com.f1betting.infrastructure.persistence.entities.BetEntity.fromDomain(BetBuilder.build())
        every { betJpaRepository.findByEventId("event1") } returns listOf(betEntity)
        val result = betRepository.findByEventId("event1")
        assertEquals(1, result.size)
        assertEquals(betEntity.id, result[0].id)
    }

    @Test
    fun `save should persist and return mapped bet`() {
        val bet = BetBuilder.build()
        val betEntity = com.f1betting.infrastructure.persistence.entities.BetEntity.fromDomain(bet)
        every { betJpaRepository.save(any()) } returns betEntity
        val result = betRepository.save(bet)
        assertEquals(bet.id, result.id)
    }

    @Test
    fun `findById should return mapped bet if present`() {
        val betEntity = com.f1betting.infrastructure.persistence.entities.BetEntity.fromDomain(BetBuilder.build())
        every { betJpaRepository.findById("bet1") } returns Optional.of(betEntity)
        val result = betRepository.findById("bet1")
        assertNotNull(result)
        assertEquals(betEntity.id, result?.id)
    }

    @Test
    fun `findById should return null if not present`() {
        every { betJpaRepository.findById("bet2") } returns Optional.empty()
        val result = betRepository.findById("bet2")
        assertNull(result)
    }
} 