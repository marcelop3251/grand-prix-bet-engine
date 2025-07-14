package com.f1betting.infrastructure.persistence.entities

import com.f1betting.builders.UserBuilder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class UserEntityTest {
    @Test
    fun `toDomain should convert UserEntity to User domain object`() {
        val userEntity = UserEntity(
            id = "user1",
            username = "testuser",
            email = "test@test.com",
            balance = BigDecimal(100),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val result = userEntity.toDomain()
        assertEquals(userEntity.id, result.id)
        assertEquals(userEntity.username, result.username)
        assertEquals(userEntity.email, result.email)
        assertEquals(userEntity.balance, result.balance)
        assertEquals(userEntity.createdAt, result.createdAt)
        assertEquals(userEntity.updatedAt, result.updatedAt)
    }

    @Test
    fun `fromDomain should convert User domain object to UserEntity`() {
        val user = UserBuilder.build()
        val result = UserEntity.fromDomain(user)
        assertEquals(user.id, result.id)
        assertEquals(user.username, result.username)
        assertEquals(user.email, result.email)
        assertEquals(user.balance, result.balance)
        assertEquals(user.createdAt, result.createdAt)
        assertEquals(user.updatedAt, result.updatedAt)
    }

    @Test
    fun `toDomain and fromDomain should be reversible`() {
        val originalUser = UserBuilder.build()
        val userEntity = UserEntity.fromDomain(originalUser)
        val convertedUser = userEntity.toDomain()
        assertEquals(originalUser.id, convertedUser.id)
        assertEquals(originalUser.username, convertedUser.username)
        assertEquals(originalUser.email, convertedUser.email)
        assertEquals(originalUser.balance, convertedUser.balance)
        assertEquals(originalUser.createdAt, convertedUser.createdAt)
        assertEquals(originalUser.updatedAt, convertedUser.updatedAt)
    }
} 