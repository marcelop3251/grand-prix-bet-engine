package com.f1betting.application.usecases.commands

data class ListEventsCommand(
    val year: Int? = null,
    val country: String? = null,
    val sessionType: String? = null
) {
    init {
        year?.let { require(it > 1900) { "Year must be after 1900" } }
        country?.let { require(it.isNotBlank()) { "Country cannot be blank" } }
        sessionType?.let { require(it.isNotBlank()) { "Session type cannot be blank" } }
    }
} 