package com.f1betting.interfaces.rest

import com.f1betting.application.usecases.GetEventDriversUseCase
import com.f1betting.application.usecases.GetEventUseCase
import com.f1betting.application.usecases.ListEventsUseCase
import com.f1betting.application.usecases.commands.ListEventsCommand
import com.f1betting.domain.exceptions.InvalidFilterParameterException
import com.f1betting.interfaces.rest.dto.EventDriverDto
import com.f1betting.interfaces.rest.dto.EventDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/events")
@Tag(name = "Events", description = "F1 Events management")
class EventController(
    private val listEventsUseCase: ListEventsUseCase,
    private val getEventUseCase: GetEventUseCase,
    private val getEventDriversUseCase: GetEventDriversUseCase
) {
    
    @GetMapping
    @Operation(summary = "List F1 events", description = "Get a list of F1 events with optional filtering")
    fun listEvents(
        @Parameter(description = "Year filter")
        @RequestParam(required = false) year: Int?,
        
        @Parameter(description = "Country filter")
        @RequestParam(required = false) country: String?,
        
        @Parameter(description = "Session type filter")
        @RequestParam(required = false) sessionType: String?
    ): ResponseEntity<List<EventDto>> {
        if (country != null && (country.isBlank() || country == "\"\"")) {
            throw InvalidFilterParameterException("country", country)
        }

        if (sessionType != null && (sessionType.isBlank() || sessionType == "\"\"")) {
            throw InvalidFilterParameterException("sessionType", sessionType)
        }
        
        val command = ListEventsCommand(
            year = year,
            country = country,
            sessionType = sessionType
        )
        val events = listEventsUseCase.execute(command)
        val eventDtos = events.map { EventDto.fromEntity(it) }
        return ResponseEntity.ok(eventDtos)
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID", description = "Get detailed information about a specific F1 event")
    fun getEvent(
        @Parameter(description = "Event ID")
        @PathVariable id: String
    ): ResponseEntity<EventDto> {
        val event = getEventUseCase.execute(id)
        return ResponseEntity.ok(EventDto.fromEntity(event))
    }
    
    @GetMapping("/{eventId}/drivers")
    @Operation(summary = "Get drivers for event", description = "Get all drivers participating in a specific event with their odds")
    fun getEventDrivers(
        @Parameter(description = "Event ID")
        @PathVariable eventId: String
    ): ResponseEntity<List<EventDriverDto>> {
        val eventDrivers = getEventDriversUseCase.execute(eventId)
        val eventDriverDtos = eventDrivers.map { EventDriverDto.fromEntity(it) }
        return ResponseEntity.ok(eventDriverDtos)
    }
} 