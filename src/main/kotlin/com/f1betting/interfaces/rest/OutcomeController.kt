package com.f1betting.interfaces.rest

import com.f1betting.application.usecases.SimulateOutcomeUseCase
import com.f1betting.application.usecases.commands.SimulateOutcomeCommand
import com.f1betting.interfaces.rest.dto.OutcomeDto
import com.f1betting.interfaces.rest.dto.request.SimulateOutcomeRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/events/{eventId}/outcome")
@Tag(name = "Outcomes", description = "Race outcome simulation")
class OutcomeController(
    private val simulateOutcomeUseCase: SimulateOutcomeUseCase
) {
    
    @PostMapping
    @Operation(summary = "Simulate race outcome", description = "Simulate the outcome of a race and settle all bets")
    fun simulateOutcome(
        @Parameter(description = "Event ID")
        @PathVariable eventId: String,
        
        @Valid @RequestBody request: SimulateOutcomeRequest
    ): ResponseEntity<OutcomeDto> {
        val command = SimulateOutcomeCommand(
            eventId = eventId,
            winnerDriverId = request.winnerDriverId
        )
        val outcome = simulateOutcomeUseCase.execute(command)
        return ResponseEntity.ok(OutcomeDto.fromEntity(outcome))
    }
} 