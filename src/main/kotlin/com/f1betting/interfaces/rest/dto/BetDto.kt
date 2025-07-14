package com.f1betting.interfaces.rest.dto

import com.f1betting.domain.entities.Bet
import com.f1betting.domain.entities.BetStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class BetDto(
    val id: String?,
    val userId: String,
    val event: EventDto,
    val driver: DriverDto,
    val amount: BigDecimal,
    val odds: BigDecimal,
    val potentialWinnings: BigDecimal,
    val status: BetStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(bet: Bet): BetDto {
            return BetDto(
                id = bet.id,
                userId = bet.user.id!!,
                event = EventDto.fromEntity(bet.event),
                driver = DriverDto.fromEntity(bet.driver),
                amount = bet.amount,
                odds = bet.odds,
                potentialWinnings = bet.potentialWinnings,
                status = bet.status,
                createdAt = bet.createdAt,
                updatedAt = bet.updatedAt
            )
        }
    }
} 