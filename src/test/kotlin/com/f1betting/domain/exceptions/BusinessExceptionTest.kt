package com.f1betting.domain.exceptions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BusinessExceptionTest {
    @Test
    fun `EntityNotFoundException should have correct type and message`() {
        val ex = EntityNotFoundException("User", "123")
        assertEquals("ENTITY_NOT_FOUND", ex.type)
        assertEquals("User not found with id: 123", ex.message)
    }

    @Test
    fun `InsufficientBalanceException should have correct type and message`() {
        val ex = InsufficientBalanceException()
        assertEquals("INSUFFICIENT_BALANCE", ex.type)
        assertEquals("Insufficient balance", ex.message)
    }

    @Test
    fun `DriverNotAvailableException should have correct type and message`() {
        val ex = DriverNotAvailableException("driver1", "event1")
        assertEquals("DRIVER_NOT_AVAILABLE", ex.type)
        assertEquals("Driver driver1 is not available for event event1", ex.message)
    }

    @Test
    fun `OutcomeAlreadyExistsException should have correct type and message`() {
        val ex = OutcomeAlreadyExistsException("event1")
        assertEquals("OUTCOME_ALREADY_EXISTS", ex.type)
        assertEquals("Outcome already exists for event: event1", ex.message)
    }

    @Test
    fun `AuthenticationRequiredException should have correct type and message`() {
        val ex = AuthenticationRequiredException()
        assertEquals("AUTHENTICATION_REQUIRED", ex.type)
        assertEquals("Authentication required", ex.message)
    }

    @Test
    fun `EventAlreadyFinishedException should have correct type and message`() {
        val ex = EventAlreadyFinishedException("event1")
        assertEquals("EVENT_ALREADY_FINISHED", ex.type)
        assertEquals("Event event1 already has an outcome and cannot accept new bets", ex.message)
    }

    @Test
    fun `InvalidFilterParameterException should have correct type and message`() {
        val ex = InvalidFilterParameterException("country", "")
        assertEquals("INVALID_FILTER_PARAMETER", ex.type)
        assertEquals("Invalid filter parameter 'country': '' cannot be empty or blank", ex.message)
    }
} 