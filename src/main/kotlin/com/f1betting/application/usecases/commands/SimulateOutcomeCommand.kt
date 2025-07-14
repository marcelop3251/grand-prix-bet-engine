package com.f1betting.application.usecases.commands

data class SimulateOutcomeCommand(
    val eventId: String,
    val winnerDriverId: String
) {
    init {
        require(eventId.isNotBlank()) { "Event ID cannot be blank" }
        require(winnerDriverId.isNotBlank()) { "Winner driver ID cannot be blank" }
    }
} 