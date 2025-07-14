package com.f1betting.application.usecases

import com.f1betting.domain.entities.Bet
import com.f1betting.domain.entities.User
import com.f1betting.domain.exceptions.EntityNotFoundException
import com.f1betting.domain.repositories.BetRepository
import com.f1betting.domain.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ListUserBetsUseCase(
    private val userRepository: UserRepository,
    private val betRepository: BetRepository
) {
    
    fun execute(username: String): List<Bet> {
        val user = findUserOrThrow(username)
        return findAllBetsForUser(user.id!!)
    }
    
    private fun findUserOrThrow(userId: String): User =
        userRepository.findByUsername(userId) ?: throw EntityNotFoundException("User", userId)
    
    private fun findAllBetsForUser(userId: String): List<Bet> {
        return betRepository.findByUserId(userId)
    }
} 