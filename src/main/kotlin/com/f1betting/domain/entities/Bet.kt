package com.f1betting.domain.entities

import java.math.BigDecimal
import java.time.LocalDateTime

data class Bet(
    val id: String? = null,
    val user: User,
    val event: Event,
    val driver: Driver,
    val amount: BigDecimal,
    val odds: BigDecimal,
    val potentialWinnings: BigDecimal,
    var status: BetStatus = BetStatus.PENDING,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun isWinningBet(winnerDriverId: String): Boolean {
        return driver.id == winnerDriverId
    }
    
    fun calculateWinnings(): BigDecimal {
        return if (status == BetStatus.WON) potentialWinnings else BigDecimal.ZERO
    }
    
    fun markAsWon() {
        status = BetStatus.WON
        updatedAt = LocalDateTime.now()
    }
    
    fun markAsLost() {
        status = BetStatus.LOST
        updatedAt = LocalDateTime.now()
    }
}

enum class BetStatus {
    PENDING, WON, LOST
} 