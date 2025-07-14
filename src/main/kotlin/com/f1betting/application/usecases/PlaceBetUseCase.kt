package com.f1betting.application.usecases

import com.f1betting.application.usecases.commands.PlaceBetCommand
import com.f1betting.domain.entities.*
import com.f1betting.domain.exceptions.*
import com.f1betting.domain.repositories.*
import java.math.BigDecimal
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PlaceBetUseCase(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository,
    private val driverRepository: DriverRepository,
    private val eventDriverRepository: EventDriverRepository,
    private val betRepository: BetRepository,
    private val outcomeRepository: OutcomeRepository
) {
    
    fun execute(command: PlaceBetCommand): Bet {
        val user = findUserOrThrow(command.userId)
        val event = findEventOrThrow(command.eventId)
        val driver = findDriverOrThrow(command.driverId)
        
        validateEventIsNotFinished(event)
        validateUserHasSufficientBalance(user, command.amount)
        
        val eventDriver = findEventDriverOrThrow(event, driver)
        val bet = createBet(user, event, driver, command.amount, eventDriver)
        
        deductBetAmountFromUserBalance(user, command.amount)
        
        return saveBet(bet)
    }
    
    private fun findUserOrThrow(userName: String): User {
        return userRepository.findByUsername(userName)
            ?: throw EntityNotFoundException("User", userName)
    }
    
    private fun findEventOrThrow(eventId: String): Event {
        return eventRepository.findById(eventId)
            ?: throw EntityNotFoundException("Event", eventId)
    }
    
    private fun findDriverOrThrow(driverId: String): Driver {
        return driverRepository.findById(driverId)
            ?: throw EntityNotFoundException("Driver", driverId)
    }
    
    private fun validateEventIsNotFinished(event: Event) {
        val eventId = requireNotNull(event.id) { "Event id must not be null" }
        val eventHasOutcome = outcomeRepository.existsByEventId(eventId)
        
        if (eventHasOutcome) {
            throw EventAlreadyFinishedException(eventId)
        }
    }
    
    private fun validateUserHasSufficientBalance(user: User, betAmount: BigDecimal) {
        val userCanPlaceBet = user.canPlaceBet(betAmount)
        
        if (!userCanPlaceBet) {
            throw InsufficientBalanceException()
        }
    }
    
    private fun findEventDriverOrThrow(event: Event, driver: Driver): EventDriver {
        val eventId = requireNotNull(event.id) { "Event id must not be null" }
        val driverId = requireNotNull(driver.id) { "Driver id must not be null" }
        
        return eventDriverRepository.findByEventIdAndDriverId(eventId, driverId)
            ?: throw DriverNotAvailableException(driverId, eventId)
    }
    
    private fun createBet(
        user: User, 
        event: Event, 
        driver: Driver, 
        amount: BigDecimal,
        eventDriver: EventDriver
    ): Bet {
        val potentialWinnings = calculatePotentialWinnings(amount, eventDriver.odds)
        
        return Bet(
            user = user,
            event = event,
            driver = driver,
            amount = amount,
            odds = eventDriver.odds,
            potentialWinnings = potentialWinnings
        )
    }
    
    private fun calculatePotentialWinnings(
        betAmount: BigDecimal,
        odds: BigDecimal
    ): BigDecimal {
        return betAmount.multiply(odds)
    }
    
    private fun deductBetAmountFromUserBalance(user: User, betAmount: BigDecimal) {
        user.placeBet(betAmount)
        userRepository.save(user)
    }
    
    private fun saveBet(bet: Bet): Bet {
        return betRepository.save(bet)
    }
} 