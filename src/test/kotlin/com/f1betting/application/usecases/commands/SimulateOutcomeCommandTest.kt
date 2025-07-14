package com.f1betting.application.usecases.commands

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class SimulateOutcomeCommandTest {
    @Test
    fun `should throw exception for blank eventId`() {
        assertThrows(IllegalArgumentException::class.java) {
            SimulateOutcomeCommand("", "driver1")
        }
    }

    @Test
    fun `should throw exception for blank winnerDriverId`() {
        assertThrows(IllegalArgumentException::class.java) {
            SimulateOutcomeCommand("event1", "")
        }
    }

    @Test
    fun `should create command with valid parameters`() {
        SimulateOutcomeCommand("event1", "driver1")
    }
} 