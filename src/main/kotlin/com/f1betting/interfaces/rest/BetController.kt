package com.f1betting.interfaces.rest

import com.f1betting.application.usecases.GetBetUseCase
import com.f1betting.application.usecases.ListUserBetsUseCase
import com.f1betting.application.usecases.PlaceBetUseCase
import com.f1betting.application.usecases.commands.PlaceBetCommand
import com.f1betting.interfaces.rest.dto.BetDto
import com.f1betting.interfaces.rest.dto.request.PlaceBetRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bets")
@Tag(name = "Bets", description = "Betting operations")
class BetController(
    private val placeBetUseCase: PlaceBetUseCase,
    private val listUserBetsUseCase: ListUserBetsUseCase,
    private val getBetUseCase: GetBetUseCase
) {
    
    @PostMapping
    @Operation(summary = "Place a bet", description = "Place a new bet on a driver for an event")
    fun placeBet(
        request: HttpServletRequest,
        @Valid @RequestBody placeBetRequest: PlaceBetRequest
    ): ResponseEntity<BetDto> {
        val userName = "user1"
        val command = PlaceBetCommand(
            userId = userName,
            eventId = placeBetRequest.eventId,
            driverId = placeBetRequest.driverId,
            amount = placeBetRequest.amount
        )
        val bet = placeBetUseCase.execute(command)
        return ResponseEntity.ok(BetDto.fromEntity(bet))
    }
    
    @GetMapping
    @Operation(summary = "List user bets", description = "Get all bets for the authenticated user")
    fun listUserBets(
        request: HttpServletRequest
    ): ResponseEntity<List<BetDto>> {
        val userId = "user1"
        val bets = listUserBetsUseCase.execute(userId)
        val betDtos = bets.map { BetDto.fromEntity(it) }
        return ResponseEntity.ok(betDtos)
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get bet by ID", description = "Get detailed information about a specific bet")
    fun getBet(
        @Parameter(description = "Bet ID")
        @PathVariable id: String
    ): ResponseEntity<BetDto> {
        val bet = getBetUseCase.execute(id)
        return ResponseEntity.ok(BetDto.fromEntity(bet))
    }
} 