package com.f1betting.domain.entities

import java.time.LocalDateTime

data class Event(
    val id: String? = null,
    val externalId: String,
    val meetingKey: Int,
    val sessionKey: Int,
    val year: Int,
    val countryName: String,
    val circuitShortName: String,
    val sessionName: String,
    val sessionType: String,
    val dateStart: LocalDateTime,
    val dateEnd: LocalDateTime? = null,
    val location: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)