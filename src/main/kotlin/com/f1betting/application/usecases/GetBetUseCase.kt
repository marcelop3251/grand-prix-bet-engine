package com.f1betting.application.usecases

import com.f1betting.domain.entities.Bet
import com.f1betting.domain.exceptions.EntityNotFoundException
import com.f1betting.domain.repositories.BetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetBetUseCase(
    private val betRepository: BetRepository
) {
    
    fun execute(betId: String): Bet {
        return findBetOrThrow(betId)
    }
    
    private fun findBetOrThrow(betId: String): Bet {
        return betRepository.findById(betId)
            ?: throw EntityNotFoundException("Bet", betId)
    }
} 