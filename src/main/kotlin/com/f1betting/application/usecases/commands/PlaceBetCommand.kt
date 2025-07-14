package com.f1betting.application.usecases.commands

import java.math.BigDecimal

data class PlaceBetCommand(
    val userId: String,
    val eventId: String,
    val driverId: String,
    val amount: BigDecimal
) {
    init {
        require(userId.isNotBlank()) { "User ID cannot be blank" }
        require(eventId.isNotBlank()) { "Event ID cannot be blank" }
        require(driverId.isNotBlank()) { "Driver ID cannot be blank" }
        require(amount > BigDecimal.ZERO) { "Amount must be positive" }
    }
} 