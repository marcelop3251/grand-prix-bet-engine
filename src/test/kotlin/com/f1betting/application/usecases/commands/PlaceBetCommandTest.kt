package com.f1betting.application.usecases.commands

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class PlaceBetCommandTest {
    @Test
    fun `should throw exception for blank userId`() {
        assertThrows(IllegalArgumentException::class.java) {
            PlaceBetCommand("", "event1", "driver1", BigDecimal.TEN)
        }
    }

    @Test
    fun `should throw exception for blank eventId`() {
        assertThrows(IllegalArgumentException::class.java) {
            PlaceBetCommand("user1", "", "driver1", BigDecimal.TEN)
        }
    }

    @Test
    fun `should throw exception for blank driverId`() {
        assertThrows(IllegalArgumentException::class.java) {
            PlaceBetCommand("user1", "event1", "", BigDecimal.TEN)
        }
    }

    @Test
    fun `should throw exception for non-positive amount`() {
        assertThrows(IllegalArgumentException::class.java) {
            PlaceBetCommand("user1", "event1", "driver1", BigDecimal.ZERO)
        }
    }

    @Test
    fun `should create command with valid parameters`() {
        PlaceBetCommand("user1", "event1", "driver1", BigDecimal.TEN)
    }
} 