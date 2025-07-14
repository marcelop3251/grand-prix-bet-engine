package com.f1betting.interfaces.rest.dto.request

import jakarta.validation.constraints.NotNull

data class SimulateOutcomeRequest(
    @field:NotNull
    val winnerDriverId: String
) 