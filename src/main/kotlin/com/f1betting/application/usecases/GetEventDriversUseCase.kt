package com.f1betting.application.usecases

import com.f1betting.domain.entities.EventDriver
import com.f1betting.domain.exceptions.EntityNotFoundException
import com.f1betting.domain.repositories.EventDriverRepository
import com.f1betting.domain.repositories.EventRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetEventDriversUseCase(
    private val eventRepository: EventRepository,
    private val eventDriverRepository: EventDriverRepository
) {
    
    fun execute(eventId: String): List<EventDriver> {
        validateEventExists(eventId)
        return findAllDriversForEvent(eventId)
    }
    
    private fun validateEventExists(eventId: String) {
        val eventExists = eventRepository.existsById(eventId)
        
        if (!eventExists) {
            throw EntityNotFoundException("Event", eventId)
        }
    }
    
    private fun findAllDriversForEvent(eventId: String): List<EventDriver> {
        return eventDriverRepository.findByEventId(eventId)
    }
} 