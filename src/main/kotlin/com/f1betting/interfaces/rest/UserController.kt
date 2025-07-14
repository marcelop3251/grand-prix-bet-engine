package com.f1betting.interfaces.rest

import com.f1betting.application.usecases.GetCurrentUserUseCase
import com.f1betting.application.usecases.ListUserBetsUseCase
import com.f1betting.interfaces.rest.dto.BetDto
import com.f1betting.interfaces.rest.dto.UserDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User profile and balance management")
class UserController(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val listUserBetsUseCase: ListUserBetsUseCase
) {
    
    @GetMapping("/me")
    @Operation(summary = "Get current user profile", description = "Get the profile and balance of the authenticated user")
    fun getCurrentUser(
        request: HttpServletRequest
    ): ResponseEntity<UserDto> {
        val user = getCurrentUserUseCase.execute("user1")
        return ResponseEntity.ok(UserDto.fromEntity(user))
    }
    
    @GetMapping("/me/bets")
    @Operation(summary = "Get user betting history", description = "Get all bets for the authenticated user")
    fun getUserBets(
        request: HttpServletRequest
    ): ResponseEntity<List<BetDto>> {
        val bets = listUserBetsUseCase.execute("user1")
        val betDtos = bets.map { BetDto.fromEntity(it) }
        return ResponseEntity.ok(betDtos)
    }
} 