package com.f1betting.infrastructure.persistence.repositories

import com.f1betting.builders.UserBuilder
import com.f1betting.infrastructure.persistence.entities.UserEntity
import com.f1betting.infrastructure.persistence.repositories.spring.UserJpaRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class UserRepositoryImplTest {
    private lateinit var userJpaRepository: UserJpaRepository
    private lateinit var userRepository: UserRepositoryImpl

    @BeforeEach
    fun setUp() {
        userJpaRepository = mockk(relaxed = true)
        userRepository = UserRepositoryImpl(userJpaRepository)
    }

    @Test
    fun `findByUsername should return mapped user`() {
        val userEntity = UserEntity.fromDomain(UserBuilder.build())
        every { userJpaRepository.findByUsername("testuser") } returns userEntity
        val result = userRepository.findByUsername("testuser")
        assertNotNull(result)
        assertEquals(userEntity.id, result?.id)
    }

    @Test
    fun `save should persist and return mapped user`() {
        val user = UserBuilder.build()
        val userEntity = UserEntity.fromDomain(user)
        every { userJpaRepository.save(any()) } returns userEntity
        val result = userRepository.save(user)
        assertEquals(user.id, result.id)
    }

    @Test
    fun `findById should return mapped user if present`() {
        val userEntity = UserEntity.fromDomain(UserBuilder.build())
        every { userJpaRepository.findById("user1") } returns Optional.of(userEntity)
        val result = userRepository.findById("user1")
        assertNotNull(result)
        assertEquals(userEntity.id, result?.id)
    }

    @Test
    fun `findById should return null if not present`() {
        every { userJpaRepository.findById("user2") } returns Optional.empty()
        val result = userRepository.findById("user2")
        assertNull(result)
    }
} 