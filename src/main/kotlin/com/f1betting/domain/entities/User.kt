package com.f1betting.domain.entities

import java.math.BigDecimal
import java.time.LocalDateTime

data class User(
    val id: String? = null,
    val username: String,
    val email: String? = null,
    var balance: BigDecimal = BigDecimal("100.00"),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun canPlaceBet(amount: BigDecimal): Boolean {
        return balance >= amount && amount > BigDecimal.ZERO
    }
    
    fun placeBet(amount: BigDecimal) {
        require(canPlaceBet(amount)) { "Insufficient balance or invalid amount" }
        balance = balance.subtract(amount)
        updatedAt = LocalDateTime.now()
    }
    
    fun addWinnings(amount: BigDecimal) {
        require(amount >= BigDecimal.ZERO) { "Winnings amount must be non-negative" }
        balance = balance.add(amount)
        updatedAt = LocalDateTime.now()
    }
} 