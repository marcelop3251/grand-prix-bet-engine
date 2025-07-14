package com.f1betting.builders

import com.f1betting.domain.entities.Outcome
import com.f1betting.domain.entities.Event
import com.f1betting.domain.entities.Driver
import java.time.LocalDateTime

class OutcomeBuilder {
    
    var id: String = "outcome1"
    var event: Event = EventBuilder.build()
    var winnerDriver: Driver = DriverBuilder.build()
    var createdAt: LocalDateTime = LocalDateTime.now()
    
    fun build() = Outcome(
        id = this.id,
        event = this.event,
        winnerDriver = this.winnerDriver,
        createdAt = this.createdAt
    )
    
    companion object {
        fun build(block: (OutcomeBuilder.() -> Unit)? = null) = when (block) {
            null -> OutcomeBuilder().build()
            else -> OutcomeBuilder().apply(block).build()
        }
    }
} 