package com.f1betting.infrastructure.persistence.repositories

import com.f1betting.domain.entities.User
import com.f1betting.domain.repositories.UserRepository
import com.f1betting.infrastructure.persistence.entities.UserEntity
import com.f1betting.infrastructure.persistence.repositories.spring.UserJpaRepository
import org.springframework.stereotype.Component

@Component
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {
    override fun findByUsername(username: String): User? =
        userJpaRepository.findByUsername(username)?.toDomain()

    override fun save(user: User): User =
        userJpaRepository.save(UserEntity.fromDomain(user)).toDomain()

    override fun findById(id: String): User? =
        userJpaRepository.findById(id).orElse(null)?.toDomain()

    override fun existsById(id: String): Boolean =
        userJpaRepository.existsById(id)

} 