package com.f1betting.application.usecases

import com.f1betting.application.usecases.commands.SimulateOutcomeCommand
import com.f1betting.domain.entities.*
import com.f1betting.domain.exceptions.*
import com.f1betting.domain.repositories.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SimulateOutcomeUseCase(
    private val eventRepository: EventRepository,
    private val driverRepository: DriverRepository,
    private val eventDriverRepository: EventDriverRepository,
    private val betRepository: BetRepository,
    private val outcomeRepository: OutcomeRepository,
    private val userRepository: UserRepository
) {
    
    fun execute(command: SimulateOutcomeCommand): Outcome {
        val event = findEventOrThrow(command.eventId)
        val winnerDriver = findDriverOrThrow(command.winnerDriverId)
        
        validateDriverIsAvailableForEvent(event, winnerDriver)
        validateOutcomeDoesNotExist(event)
        
        val outcome = createAndSaveOutcome(event, winnerDriver)
        processAllBetsForEvent(event, winnerDriver)
        
        return outcome
    }
    
    private fun findEventOrThrow(eventId: String): Event {
        return eventRepository.findById(eventId)
            ?: throw EntityNotFoundException("Event", eventId)
    }
    
    private fun findDriverOrThrow(driverId: String): Driver {
        return driverRepository.findById(driverId)
            ?: throw EntityNotFoundException("Driver", driverId)
    }
    
    private fun validateDriverIsAvailableForEvent(event: Event, driver: Driver) {
        val eventId = requireNotNull(event.id) { "Event id must not be null" }
        val driverId = requireNotNull(driver.id) { "Driver id must not be null" }
        
        val isDriverAvailable = eventDriverRepository.existsByEventIdAndDriverId(eventId, driverId)
        if (!isDriverAvailable) {
            throw DriverNotAvailableException(driverId, eventId)
        }
    }
    
    private fun validateOutcomeDoesNotExist(event: Event) {
        val eventId = requireNotNull(event.id) { "Event id must not be null" }
        val outcomeAlreadyExists = outcomeRepository.existsByEventId(eventId)
        
        if (outcomeAlreadyExists) {
            throw OutcomeAlreadyExistsException(eventId)
        }
    }
    
    private fun createAndSaveOutcome(event: Event, winnerDriver: Driver): Outcome {
        val outcome = Outcome(
            event = event,
            winnerDriver = winnerDriver
        )
        return outcomeRepository.save(outcome)
    }
    
    private fun processAllBetsForEvent(event: Event, winnerDriver: Driver) {
        val eventId = requireNotNull(event.id) { "Event id must not be null" }
        val winnerDriverId = requireNotNull(winnerDriver.id) { "Driver id must not be null" }
        
        val allBetsForEvent = betRepository.findByEventId(eventId)
        
        allBetsForEvent.forEach { bet ->
            processBet(bet, winnerDriverId)
        }
    }
    
    private fun processBet(bet: Bet, winnerDriverId: String) {
        when {
            bet.isWinningBet(winnerDriverId) -> processWinningBet(bet)
            else -> processLosingBet(bet)
        }
        betRepository.save(bet)
    }
    
    private fun processWinningBet(bet: Bet) {
        bet.markAsWon()
        val winnings = bet.calculateWinnings()
        bet.user.addWinnings(winnings)
        userRepository.save(bet.user)
    }
    
    private fun processLosingBet(bet: Bet) {
        bet.markAsLost()
    }
} 