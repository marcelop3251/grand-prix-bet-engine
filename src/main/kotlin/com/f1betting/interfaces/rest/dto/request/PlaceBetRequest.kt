package com.f1betting.interfaces.rest.dto.request

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class PlaceBetRequest(
    @field:NotNull
    val eventId: String,
    
    @field:NotNull
    val driverId: String,
    
    @field:NotNull
    @field:DecimalMin("1.0")
    val amount: BigDecimal
) 