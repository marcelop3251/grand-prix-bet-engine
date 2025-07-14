package com.f1betting.infrastructure.persistence.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Column
import java.time.LocalDateTime
import com.f1betting.domain.entities.Event
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType

@Entity
@Table(name = "events")
data class EventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    val externalId: String,
    val meetingKey: Int,
    val sessionKey: Int,
    @Column(name = "\"year\"")
    val year: Int,
    val countryName: String,
    val circuitShortName: String,
    val sessionName: String,
    val sessionType: String,
    val dateStart: LocalDateTime,
    val dateEnd: LocalDateTime? = null,
    val location: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain(): Event = Event(
        id = id,
        externalId = externalId,
        meetingKey = meetingKey,
        sessionKey = sessionKey,
        year = year,
        countryName = countryName,
        circuitShortName = circuitShortName,
        sessionName = sessionName,
        sessionType = sessionType,
        dateStart = dateStart,
        dateEnd = dateEnd,
        location = location,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(event: Event): EventEntity = EventEntity(
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
            location = event.location,
            createdAt = event.createdAt,
            updatedAt = event.updatedAt
        )
    }
} 