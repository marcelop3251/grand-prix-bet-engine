package com.f1betting.interfaces.rest.dto

import com.f1betting.domain.entities.User
import java.math.BigDecimal
import java.time.LocalDateTime

data class UserDto(
    val id: String?,
    val username: String,
    val email: String?,
    val balance: BigDecimal,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(user: User): UserDto {
            return UserDto(
                id = user.id,
                username = user.username,
                email = user.email,
                balance = user.balance,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }
    }
} 