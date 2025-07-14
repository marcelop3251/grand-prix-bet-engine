package com.f1betting.interfaces.rest.dto.request

import jakarta.validation.Validation
import jakarta.validation.Validator
import java.math.BigDecimal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PlaceBetRequestTest {
    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `should create PlaceBetRequest with valid data`() {
        val request = PlaceBetRequest(
            eventId = "event1",
            driverId = "driver1",
            amount = BigDecimal(10)
        )
        assertEquals("event1", request.eventId)
        assertEquals("driver1", request.driverId)
        assertEquals(BigDecimal(10), request.amount)
    }

    @Test
    fun `should validate when all fields are valid`() {
        val request = PlaceBetRequest(
            eventId = "event1",
            driverId = "driver1",
            amount = BigDecimal(10)
        )
        val violations = validator.validate(request)
        assertEquals(0, violations.size)
    }

    @Test
    fun `should fail validation when amount is less than minimum`() {
        val request = PlaceBetRequest(
            eventId = "event1",
            driverId = "driver1",
            amount = BigDecimal("0.5")
        )
        val violations = validator.validate(request)
        assertEquals(1, violations.size)
        assertEquals("amount", violations.first().propertyPath.toString())
    }
} 