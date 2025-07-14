package com.f1betting.infrastructure.persistence.entities

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import com.f1betting.domain.entities.Bet
import com.f1betting.domain.entities.BetStatus

@Entity
@Table(name = "bets")
data class BetEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    val event: EventEntity,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    val driver: DriverEntity,
    
    val amount: BigDecimal,
    val odds: BigDecimal,
    val potentialWinnings: BigDecimal,
    
    @Enumerated(EnumType.STRING)
    var status: BetStatus = BetStatus.PENDING,
    
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain(): Bet = Bet(
        id = id,
        user = user.toDomain(),
        event = event.toDomain(),
        driver = driver.toDomain(),
        amount = amount,
        odds = odds,
        potentialWinnings = potentialWinnings,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(bet: Bet): BetEntity = BetEntity(
            id = bet.id,
            user = UserEntity.fromDomain(bet.user),
            event = EventEntity.fromDomain(bet.event),
            driver = DriverEntity.fromDomain(bet.driver),
            amount = bet.amount,
            odds = bet.odds,
            potentialWinnings = bet.potentialWinnings,
            status = bet.status,
            createdAt = bet.createdAt,
            updatedAt = bet.updatedAt
        )
    }
} 