package com.f1betting.infrastructure.persistence.repositories

import com.f1betting.builders.OutcomeBuilder
import com.f1betting.infrastructure.persistence.entities.OutcomeEntity
import com.f1betting.infrastructure.persistence.repositories.spring.OutcomeJpaRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OutcomeRepositoryImplTest {
    private lateinit var outcomeJpaRepository: OutcomeJpaRepository
    private lateinit var outcomeRepository: OutcomeRepositoryImpl

    @BeforeEach
    fun setUp() {
        outcomeJpaRepository = mockk(relaxed = true)
        outcomeRepository = OutcomeRepositoryImpl(outcomeJpaRepository)
    }

    @Test
    fun `existsByEventId should return boolean`() {
        every { outcomeJpaRepository.existsByEventId("event1") } returns true
        val result = outcomeRepository.existsByEventId("event1")
        assertTrue(result)
    }

    @Test
    fun `save should persist and return mapped outcome`() {
        val outcome = OutcomeBuilder.build()
        val outcomeEntity = OutcomeEntity.fromDomain(outcome)
        every { outcomeJpaRepository.save(any()) } returns outcomeEntity
        val result = outcomeRepository.save(outcome)
        assertEquals(outcome.id, result.id)
    }
} 