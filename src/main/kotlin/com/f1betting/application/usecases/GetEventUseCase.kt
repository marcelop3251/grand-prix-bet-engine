package com.f1betting.application.usecases

import com.f1betting.domain.entities.Event
import com.f1betting.domain.exceptions.EntityNotFoundException
import com.f1betting.domain.repositories.EventRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetEventUseCase(
    private val eventRepository: EventRepository
) {
    
    fun execute(eventId: String): Event {
        return eventRepository.findById(eventId)
            ?: throw EntityNotFoundException("Event", eventId)
    }
} 