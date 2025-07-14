package com.f1betting.infrastructure.gateway.openf1.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

data class OpenF1SessionDto(
    @JsonProperty("session_key")
    val sessionKey: Int,
    
    @JsonProperty("meeting_key")
    val meetingKey: Int,
    
    @JsonProperty("session_name")
    val sessionName: String,
    
    @JsonProperty("session_type")
    val sessionType: String,
    
    @JsonProperty("date_start")
    val dateStart: ZonedDateTime,
    
    @JsonProperty("date_end")
    val dateEnd: ZonedDateTime?,
    
    @JsonProperty("year")
    val year: Int,
    
    @JsonProperty("country_name")
    val countryName: String,
    
    @JsonProperty("circuit_short_name")
    val circuitShortName: String,
    
    @JsonProperty("location")
    val location: String?
)

data class OpenF1DriverDto(
    @JsonProperty("driver_number")
    val driverNumber: Int,
    
    @JsonProperty("first_name")
    val firstName: String,
    
    @JsonProperty("last_name")
    val lastName: String,
    
    @JsonProperty("full_name")
    val fullName: String,
    
    @JsonProperty("broadcast_name")
    val broadcastName: String?,
    
    @JsonProperty("name_acronym")
    val nameAcronym: String?,
    
    @JsonProperty("country_code")
    val countryCode: String?,
    
    @JsonProperty("team_name")
    val teamName: String?,
    
    @JsonProperty("team_colour")
    val teamColour: String?,
    
    @JsonProperty("headshot_url")
    val headshotUrl: String?,
    
    @JsonProperty("session_key")
    val sessionKey: Int
) 