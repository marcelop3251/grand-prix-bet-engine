package com.f1betting.application.usecases.commands

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class ListEventsCommandTest {
    @Test
    fun `should throw exception for invalid year`() {
        assertThrows(IllegalArgumentException::class.java) {
            ListEventsCommand(year = 1800)
        }
    }

    @Test
    fun `should throw exception for blank country`() {
        assertThrows(IllegalArgumentException::class.java) {
            ListEventsCommand(country = "   ")
        }
    }

    @Test
    fun `should throw exception for blank sessionType`() {
        assertThrows(IllegalArgumentException::class.java) {
            ListEventsCommand(sessionType = "")
        }
    }

    @Test
    fun `should create command with valid parameters`() {
        ListEventsCommand(year = 2024, country = "BR", sessionType = "Race")
    }
} 