package com.f1betting.interfaces.rest.dto.request

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SimulateOutcomeRequestTest {
    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `should create SimulateOutcomeRequest with valid data`() {
        val request = SimulateOutcomeRequest(winnerDriverId = "driver1")
        assertEquals("driver1", request.winnerDriverId)
    }

    @Test
    fun `should validate when winnerDriverId is not null`() {
        val request = SimulateOutcomeRequest(winnerDriverId = "driver1")
        val violations = validator.validate(request)
        assertEquals(0, violations.size)
    }


} 