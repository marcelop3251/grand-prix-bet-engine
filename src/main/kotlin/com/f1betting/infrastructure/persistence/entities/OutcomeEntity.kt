package com.f1betting.infrastructure.persistence.entities

import jakarta.persistence.*
import java.time.LocalDateTime
import com.f1betting.domain.entities.Outcome

@Entity
@Table(name = "outcomes")
data class OutcomeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false, unique = true)
    val event: EventEntity,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_driver_id", nullable = false)
    val winnerDriver: DriverEntity,
    
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain(): Outcome = Outcome(
        id = id,
        event = event.toDomain(),
        winnerDriver = winnerDriver.toDomain(),
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(outcome: Outcome): OutcomeEntity = OutcomeEntity(
            id = outcome.id,
            event = EventEntity.fromDomain(outcome.event),
            winnerDriver = DriverEntity.fromDomain(outcome.winnerDriver),
            createdAt = outcome.createdAt
        )
    }
} 