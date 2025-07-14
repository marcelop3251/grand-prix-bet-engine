package com.f1betting.interfaces.rest.dto

import com.f1betting.builders.UserBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UserDtoTest {
    @Test
    fun `fromEntity should convert User to UserDto`() {
        val user = UserBuilder.build()
        val result = UserDto.fromEntity(user)
        assertEquals(user.id, result.id)
        assertEquals(user.username, result.username)
        assertEquals(user.email, result.email)
        assertEquals(user.balance, result.balance)
        assertEquals(user.createdAt, result.createdAt)
        assertEquals(user.updatedAt, result.updatedAt)
    }
} 