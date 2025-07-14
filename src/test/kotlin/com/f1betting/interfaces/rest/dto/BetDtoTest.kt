package com.f1betting.interfaces.rest.dto

import com.f1betting.builders.BetBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BetDtoTest {
    @Test
    fun `fromEntity should convert Bet to BetDto`() {
        val bet = BetBuilder.build()
        val result = BetDto.fromEntity(bet)
        assertEquals(bet.id, result.id)
        assertEquals(bet.user.id, result.userId)
        assertEquals(bet.amount, result.amount)
        assertEquals(bet.odds, result.odds)
        assertEquals(bet.potentialWinnings, result.potentialWinnings)
        assertEquals(bet.status, result.status)
        assertEquals(bet.createdAt, result.createdAt)
        assertEquals(bet.updatedAt, result.updatedAt)
        assertEquals(bet.event.id, result.event.id)
        assertEquals(bet.driver.id, result.driver.id)
    }
} 