package com.f1betting.domain.repositories

import com.f1betting.domain.entities.User

interface UserRepository {
    fun findByUsername(username: String): User?
    fun save(user: User): User
    fun findById(id: String): User?
    fun existsById(id: String): Boolean
} 