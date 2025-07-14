package com.f1betting.interfaces.rest.dto

import com.f1betting.domain.entities.EventDriver
import java.math.BigDecimal
import java.time.LocalDateTime

data class EventDriverDto(
    val id: String?,
    val eventId: String,
    val driver: DriverDto,
    val odds: BigDecimal,
    val createdAt: LocalDateTime
) {
    companion object {
        fun fromEntity(eventDriver: EventDriver): EventDriverDto {
            return EventDriverDto(
                id = eventDriver.id,
                eventId = eventDriver.event.id!!,
                driver = DriverDto.fromEntity(eventDriver.driver),
                odds = eventDriver.odds,
                createdAt = eventDriver.createdAt
            )
        }
    }
} 