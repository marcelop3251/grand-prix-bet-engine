package com.f1betting.infrastructure.persistence.repositories

import com.f1betting.domain.entities.Bet
import com.f1betting.domain.repositories.BetRepository
import com.f1betting.infrastructure.persistence.entities.BetEntity
import com.f1betting.infrastructure.persistence.repositories.spring.BetJpaRepository
import org.springframework.stereotype.Component

@Component
class BetRepositoryImpl(
    private val betJpaRepository: BetJpaRepository
) : BetRepository {
    override fun findByUserId(userId: String): List<Bet> =
        betJpaRepository.findByUserId(userId).map { it.toDomain() }

    override fun findByEventId(eventId: String): List<Bet> =
        betJpaRepository.findByEventId(eventId).map { it.toDomain() }

    override fun save(bet: Bet): Bet =
        betJpaRepository.save(BetEntity.fromDomain(bet)).toDomain()

    override fun findById(id: String): Bet? =
        betJpaRepository.findById(id).orElse(null)?.toDomain()
} 