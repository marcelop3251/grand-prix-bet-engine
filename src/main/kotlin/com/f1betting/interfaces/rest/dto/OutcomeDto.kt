package com.f1betting.interfaces.rest.dto

import com.f1betting.domain.entities.Outcome
import java.time.LocalDateTime

data class OutcomeDto(
    val id: String?,
    val event: EventDto,
    val winnerDriver: DriverDto,
    val createdAt: LocalDateTime
) {
    companion object {
        fun fromEntity(outcome: Outcome): OutcomeDto = OutcomeDto(
            id = outcome.id,
            event = EventDto.fromEntity(outcome.event),
            winnerDriver = DriverDto.fromEntity(outcome.winnerDriver),
            createdAt = outcome.createdAt
        )
    }
} 