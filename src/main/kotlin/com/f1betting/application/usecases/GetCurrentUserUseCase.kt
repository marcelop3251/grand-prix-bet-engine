package com.f1betting.application.usecases

import com.f1betting.domain.entities.User
import com.f1betting.domain.exceptions.EntityNotFoundException
import com.f1betting.domain.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetCurrentUserUseCase(
    private val userRepository: UserRepository
) {
    
    fun execute(username: String): User {
        return findUserByUsernameOrThrow(username)
    }
    
    private fun findUserByUsernameOrThrow(username: String): User {
        return userRepository.findByUsername(username)
            ?: throw EntityNotFoundException("User", username)
    }
} 