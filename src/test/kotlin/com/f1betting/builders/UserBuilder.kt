package com.f1betting.builders

import com.f1betting.domain.entities.User
import java.math.BigDecimal
import java.time.LocalDateTime

class UserBuilder {
    
    var id: String = "user1"
    var username: String = "testuser"
    var email: String = "test@test.com"
    var balance: BigDecimal = BigDecimal("100.00")
    var createdAt: LocalDateTime = LocalDateTime.now()
    var updatedAt: LocalDateTime = LocalDateTime.now()
    
    fun build() = User(
        id = this.id,
        username = this.username,
        email = this.email,
        balance = this.balance,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
    
    companion object {
        fun build(block: (UserBuilder.() -> Unit)? = null) = when (block) {
            null -> UserBuilder().build()
            else -> UserBuilder().apply(block).build()
        }
    }
} 