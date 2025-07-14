package com.f1betting.builders

import com.f1betting.domain.entities.EventDriver
import com.f1betting.domain.entities.Event
import com.f1betting.domain.entities.Driver
import java.math.BigDecimal
import java.time.LocalDateTime

class EventDriverBuilder {
    
    var id: String = "ed1"
    var event: Event = EventBuilder.build()
    var driver: Driver = DriverBuilder.build()
    var odds: BigDecimal = BigDecimal("2.00")
    var createdAt: LocalDateTime = LocalDateTime.now()
    
    fun build() = EventDriver(
        id = this.id,
        event = this.event,
        driver = this.driver,
        odds = this.odds,
        createdAt = this.createdAt
    )
    
    companion object {
        fun build(block: (EventDriverBuilder.() -> Unit)? = null) = when (block) {
            null -> EventDriverBuilder().build()
            else -> EventDriverBuilder().apply(block).build()
        }
    }
} 