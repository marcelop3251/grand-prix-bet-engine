package com.f1betting.infrastructure.gateway.openf1

import com.f1betting.infrastructure.gateway.openf1.dto.OpenF1DriverDto
import com.f1betting.infrastructure.gateway.openf1.dto.OpenF1SessionDto
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class OpenF1Client(
    @Qualifier("openF1WebClient")
    private val webClient: WebClient
) {
    
    suspend fun getSessions(
        year: Int? = null,
        country: String? = null,
        sessionType: String? = null
    ): List<OpenF1SessionDto> {
        val queryParams = buildString {
            append("?")
            if (year != null) append("year=$year&")
            if (country != null) append("country_name=$country&")
            if (sessionType != null) append("session_type=$sessionType&")
            if (endsWith("&")) deleteCharAt(length - 1)
            if (isEmpty()) deleteCharAt(0) // Remove the "?" if no params
        }
        
        return webClient.get()
            .uri("/sessions$queryParams")
            .retrieve()
            .awaitBody()
    }
    
    suspend fun getDrivers(sessionKey: Int): List<OpenF1DriverDto> {
        return webClient.get()
            .uri("/drivers?session_key=$sessionKey")
            .retrieve()
            .awaitBody()
    }
    
    suspend fun getDriversByYear(year: Int): List<OpenF1DriverDto> {
        return webClient.get()
            .uri("/drivers?year=$year")
            .retrieve()
            .awaitBody()
    }
} 