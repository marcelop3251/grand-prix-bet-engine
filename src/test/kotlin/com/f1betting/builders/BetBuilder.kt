package com.f1betting.builders

import com.f1betting.domain.entities.Bet
import com.f1betting.domain.entities.BetStatus
import com.f1betting.domain.entities.Driver
import com.f1betting.domain.entities.Event
import com.f1betting.domain.entities.User
import java.math.BigDecimal
import java.time.LocalDateTime

class BetBuilder {
    
    var id: String = "bet1"
    var user: User = UserBuilder.build()
    var event: Event = EventBuilder.build()
    var driver: Driver = DriverBuilder.build()
    var amount: BigDecimal = BigDecimal("10.00")
    var odds: BigDecimal = BigDecimal("2.00")
    var potentialWinnings: BigDecimal = BigDecimal("20.00")
    var status: BetStatus = BetStatus.PENDING
    var createdAt: LocalDateTime = LocalDateTime.now()
    var updatedAt: LocalDateTime = LocalDateTime.now()
    
    fun build() = Bet(
        id = this.id,
        user = this.user,
        event = this.event,
        driver = this.driver,
        amount = this.amount,
        odds = this.odds,
        potentialWinnings = this.potentialWinnings,
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
    
    companion object {
        fun build(block: (BetBuilder.() -> Unit)? = null) = when (block) {
            null -> BetBuilder().build()
            else -> BetBuilder().apply(block).build()
        }
    }
} 