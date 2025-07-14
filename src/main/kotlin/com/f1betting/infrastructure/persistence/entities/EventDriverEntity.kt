package com.f1betting.infrastructure.persistence.entities

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import com.f1betting.domain.entities.EventDriver

@Entity
@Table(name = "event_drivers")
data class EventDriverEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    val event: EventEntity,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    val driver: DriverEntity,
    
    val odds: BigDecimal,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain(): EventDriver = EventDriver(
        id = id,
        event = event.toDomain(),
        driver = driver.toDomain(),
        odds = odds,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(eventDriver: EventDriver): EventDriverEntity = EventDriverEntity(
            id = eventDriver.id,
            event = EventEntity.fromDomain(eventDriver.event),
            driver = DriverEntity.fromDomain(eventDriver.driver),
            odds = eventDriver.odds,
            createdAt = eventDriver.createdAt
        )
    }
} 