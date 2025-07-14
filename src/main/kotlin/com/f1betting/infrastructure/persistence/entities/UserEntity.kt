package com.f1betting.infrastructure.persistence.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import com.f1betting.domain.entities.User
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    val username: String,
    val email: String? = null,
    var balance: BigDecimal = BigDecimal("100.00"),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain(): User = User(
        id = id,
        username = username,
        email = email,
        balance = balance,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(user: User): UserEntity = UserEntity(
            id = user.id,
            username = user.username,
            email = user.email,
            balance = user.balance,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
} 