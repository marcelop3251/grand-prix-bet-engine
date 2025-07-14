package com.f1betting.infrastructure.persistence.repositories.spring

import com.f1betting.infrastructure.persistence.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJpaRepository : JpaRepository<UserEntity, String> {
    fun findByUsername(username: String): UserEntity?
} 