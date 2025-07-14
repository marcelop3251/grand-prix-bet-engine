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
class GetUserBetsUseCase(
    private val userRepository: UserRepository,
    private val betRepository: BetRepository
) {
    
    fun execute(username: String): List<Bet> {
        val user = findUserByUsernameOrThrow(username)
        return findAllBetsForUser(user)
    }
    
    private fun findUserByUsernameOrThrow(username: String): User {
        return userRepository.findByUsername(username)
            ?: throw EntityNotFoundException("User", username)
    }
    
    private fun findAllBetsForUser(user: User): List<Bet> {
        val userId = requireNotNull(user.id) { "User id must not be null" }
        return betRepository.findByUserId(userId)
    }
} 