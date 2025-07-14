package com.f1betting.domain.entities

import java.math.BigDecimal
import java.time.LocalDateTime

data class EventDriver(
    val id: String? = null,
    val event: Event,
    val driver: Driver,
    val odds: BigDecimal,
    val createdAt: LocalDateTime = LocalDateTime.now()
) 