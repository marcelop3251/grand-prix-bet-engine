package com.f1betting.domain.repositories

import com.f1betting.domain.entities.Outcome

interface OutcomeRepository {
    fun existsByEventId(eventId: String): Boolean
    fun save(outcome: Outcome): Outcome
} 