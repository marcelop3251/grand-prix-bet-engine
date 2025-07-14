package com.f1betting.domain.entities

import java.time.LocalDateTime

data class Driver(
    val id: String? = null,
    val externalId: String,
    val driverNumber: Int,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val broadcastName: String? = null,
    val nameAcronym: String? = null,
    val countryCode: String? = null,
    val teamName: String? = null,
    val teamColour: String? = null,
    val headshotUrl: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun getDisplayName(): String = broadcastName ?: fullName
} 