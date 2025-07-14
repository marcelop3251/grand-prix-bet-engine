package com.f1betting.domain.repositories

import com.f1betting.domain.entities.Bet
import com.f1betting.domain.entities.BetStatus

interface BetRepository {
    fun findByUserId(userId: String): List<Bet>
    fun findByEventId(eventId: String): List<Bet>
    fun save(bet: Bet): Bet
    fun findById(id: String): Bet?
} 