package com.f1betting.infrastructure.persistence.repositories

import com.f1betting.domain.entities.Outcome
import com.f1betting.domain.repositories.OutcomeRepository
import com.f1betting.infrastructure.persistence.entities.OutcomeEntity
import com.f1betting.infrastructure.persistence.repositories.spring.OutcomeJpaRepository
import org.springframework.stereotype.Component

@Component
class OutcomeRepositoryImpl(
    private val outcomeJpaRepository: OutcomeJpaRepository
) : OutcomeRepository {

    override fun existsByEventId(eventId: String): Boolean =
        outcomeJpaRepository.existsByEventId(eventId)

    override fun save(outcome: Outcome): Outcome =
        outcomeJpaRepository.save(OutcomeEntity.fromDomain(outcome)).toDomain()
} 