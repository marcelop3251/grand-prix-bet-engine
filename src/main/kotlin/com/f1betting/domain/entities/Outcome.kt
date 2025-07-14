package com.f1betting.domain.entities

import java.time.LocalDateTime

data class Outcome(
    val id: String? = null,
    val event: Event,
    val winnerDriver: Driver,
    val createdAt: LocalDateTime = LocalDateTime.now()
) 