package com.f1betting.interfaces.rest.dto

import com.f1betting.domain.entities.Event
import java.time.LocalDateTime

data class EventDto(
    val id: String?,
    val externalId: String,
    val meetingKey: Int,
    val sessionKey: Int,
    val year: Int,
    val countryName: String,
    val circuitShortName: String,
    val sessionName: String,
    val sessionType: String,
    val dateStart: LocalDateTime,
    val dateEnd: LocalDateTime?,
    val location: String?
) {
    companion object {
        fun fromEntity(event: Event): EventDto {
            return EventDto(
                id = event.id,
                externalId = event.externalId,
                meetingKey = event.meetingKey,
                sessionKey = event.sessionKey,
                year = event.year,
                countryName = event.countryName,
                circuitShortName = event.circuitShortName,
                sessionName = event.sessionName,
                sessionType = event.sessionType,
                dateStart = event.dateStart,
                dateEnd = event.dateEnd,
                location = event.location
            )
        }
    }
} 