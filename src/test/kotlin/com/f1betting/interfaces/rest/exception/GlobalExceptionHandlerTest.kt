package com.f1betting.interfaces.rest.exception

import com.f1betting.domain.exceptions.AuthenticationRequiredException
import com.f1betting.domain.exceptions.BusinessException
import com.f1betting.domain.exceptions.DriverNotAvailableException
import com.f1betting.domain.exceptions.EntityNotFoundException
import com.f1betting.domain.exceptions.EventAlreadyFinishedException
import com.f1betting.domain.exceptions.InsufficientBalanceException
import com.f1betting.domain.exceptions.InvalidFilterParameterException
import com.f1betting.domain.exceptions.OutcomeAlreadyExistsException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest

class GlobalExceptionHandlerTest {
    private lateinit var handler: GlobalExceptionHandler
    private lateinit var request: WebRequest

    @BeforeEach
    fun setUp() {
        handler = GlobalExceptionHandler()
        val mockRequest = MockHttpServletRequest()
        mockRequest.requestURI = "/api/test"
        request = ServletWebRequest(mockRequest)
    }

    @Test
    fun `handleBusinessException should return NOT_FOUND for EntityNotFoundException`() {
        val ex = EntityNotFoundException("User", "user1")
        val response = handler.handleBusinessException(ex, request)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("User not found with id: user1", response.body?.message)
        assertEquals("ENTITY_NOT_FOUND", response.body?.type)
    }

    @Test
    fun `handleBusinessException should return BAD_REQUEST for InsufficientBalanceException`() {
        val ex = InsufficientBalanceException()
        val response = handler.handleBusinessException(ex, request)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Insufficient balance", response.body?.message)
        assertEquals("INSUFFICIENT_BALANCE", response.body?.type)
    }

    @Test
    fun `handleBusinessException should return BAD_REQUEST for DriverNotAvailableException`() {
        val ex = DriverNotAvailableException("driver1", "event1")
        val response = handler.handleBusinessException(ex, request)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Driver driver1 is not available for event event1", response.body?.message)
        assertEquals("DRIVER_NOT_AVAILABLE", response.body?.type)
    }

    @Test
    fun `handleBusinessException should return CONFLICT for OutcomeAlreadyExistsException`() {
        val ex = OutcomeAlreadyExistsException("event1")
        val response = handler.handleBusinessException(ex, request)
        assertEquals(HttpStatus.CONFLICT, response.statusCode)
        assertEquals("Outcome already exists for event: event1", response.body?.message)
        assertEquals("OUTCOME_ALREADY_EXISTS", response.body?.type)
    }

    @Test
    fun `handleBusinessException should return BAD_REQUEST for EventAlreadyFinishedException`() {
        val ex = EventAlreadyFinishedException("event1")
        val response = handler.handleBusinessException(ex, request)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Event event1 already has an outcome and cannot accept new bets", response.body?.message)
        assertEquals("EVENT_ALREADY_FINISHED", response.body?.type)
    }

    @Test
    fun `handleBusinessException should return BAD_REQUEST for InvalidFilterParameterException`() {
        val ex = InvalidFilterParameterException("country", "invalid")
        val response = handler.handleBusinessException(ex, request)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Invalid filter parameter 'country': 'invalid' cannot be empty or blank", response.body?.message)
        assertEquals("INVALID_FILTER_PARAMETER", response.body?.type)
    }

    @Test
    fun `handleBusinessException should return UNAUTHORIZED for AuthenticationRequiredException`() {
        val ex = AuthenticationRequiredException()
        val response = handler.handleBusinessException(ex, request)
        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
        assertEquals("Authentication required", response.body?.message)
        assertEquals("AUTHENTICATION_REQUIRED", response.body?.type)
    }

    @Test
    fun `handleBusinessException should return BAD_REQUEST for generic BusinessException`() {
        val ex = object : BusinessException("Generic error", "GenericException") {}
        val response = handler.handleBusinessException(ex, request)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("GenericException", response.body?.message)
        assertEquals("Generic error", response.body?.type)
    }

    @Test
    fun `handleIllegalArgumentException should return BAD_REQUEST`() {
        val ex = IllegalArgumentException("Invalid argument")
        val response = handler.handleIllegalArgumentException(ex, request)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Invalid argument", response.body?.message)
        assertEquals("Bad Request", response.body?.error)
    }

    @Test
    fun `handleGenericException should return INTERNAL_SERVER_ERROR`() {
        val ex = Exception("Unexpected error")
        val response = handler.handleGenericException(ex, request)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertEquals("An unexpected error occurred", response.body?.message)
        assertEquals("Internal Server Error", response.body?.error)
    }
} 