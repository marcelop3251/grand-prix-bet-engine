package com.f1betting.builders

import com.f1betting.domain.entities.Event
import java.time.LocalDateTime

class EventBuilder {
    
    var id: String = "event1"
    var externalId: String = "ext1"
    var meetingKey: Int = 1
    var sessionKey: Int = 1
    var year: Int = 2024
    var countryName: String = "BR"
    var circuitShortName: String = "BR"
    var sessionName: String = "GP Brasil"
    var sessionType: String = "Race"
    var dateStart: LocalDateTime = LocalDateTime.now()
    var dateEnd: LocalDateTime? = null
    var location: String? = null
    var createdAt: LocalDateTime = LocalDateTime.now()
    var updatedAt: LocalDateTime = LocalDateTime.now()
    
    fun build() = Event(
        id = this.id,
        externalId = this.externalId,
        meetingKey = this.meetingKey,
        sessionKey = this.sessionKey,
        year = this.year,
        countryName = this.countryName,
        circuitShortName = this.circuitShortName,
        sessionName = this.sessionName,
        sessionType = this.sessionType,
        dateStart = this.dateStart,
        dateEnd = this.dateEnd,
        location = this.location,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
    
    companion object {
        fun build(block: (EventBuilder.() -> Unit)? = null) = when (block) {
            null -> EventBuilder().build()
            else -> EventBuilder().apply(block).build()
        }
    }
} 